package edu.sjsu.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import edu.sjsu.exceptions.InterviewException;
import edu.sjsu.exceptions.JobApplicationExceptions;
import edu.sjsu.models.Interview;
import edu.sjsu.models.JobApplication;
import edu.sjsu.repositories.InterviewRepository;
import edu.sjsu.repositories.JobApplicationRepository;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.UidGenerator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class InterviewService {

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	JobApplicationService jobApplicationService;

	@Autowired
	JobApplicationRepository jobApplicationRepository;

	@Autowired
	InterviewRepository interviewRepository;

	@Autowired
	EmailService emailService;

	public JobApplication scheduleInterview(Map<String, Object> params) throws JobApplicationExceptions {

		String jobApplicationId = (String) params.get("jobApplicationId");
		String startTimeString = (String) params.get("startTime");
		String endTimeString = (String) params.get("endTime");
		Date startTime = null;
		Date endTime = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:hh-mm-ss");
		try {
			startTime = formatter.parse(startTimeString);
			endTime = formatter.parse(endTimeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		JobApplication jobApplication = jobApplicationRepository.findByApplicationId(jobApplicationId);
		if (null == jobApplication) {
			throw new JobApplicationExceptions("No job Application found");
		}
		Interview interview = new Interview();
		interview.setStartTime(startTime);
		interview.setEndTime(endTime);
		interview.setStatus(0); // 0- status pending
		interviewRepository.save(interview);
		jobApplication.getInterviews().add(interview);
		jobApplicationRepository.save(jobApplication);
		return jobApplication;
	}

	public Boolean updateInterview(Map<String, Object> params) throws InterviewException {

		String applicationId = (String) params.get("jobApplicationId");
		int interviewNo = Integer.parseInt((String) params.get("interviewNo"));
		int status = Integer.parseInt((String) params.get("status"));
		Interview interview = interviewRepository.findByInterviewNo(interviewNo);
		if (status == 3) {
			interview.setStatus(status);
		} else {
			if (interview.getStatus() != 0) {
				throw new InterviewException("You can not update the status of interview at this stage");
			}
			JobApplication jobApp = jobApplicationRepository.findByApplicationId(applicationId);
			StringBuilder message = new StringBuilder();
			if (status == 1) {
				message.append("The applicant has 'ACCEPTED' the interview.\n");
			}
			if (status == 2) {
				message.append("The applicant has 'REJECTED' the interview.\n");
			}
			message.append("\nPosition : " + jobApp.getJobPosting().getTitle());
			message.append("\nJob Requisition Id : " + jobApp.getJobPosting().getRequisitionId());
			message.append("\nApplication Id : " + jobApp.getApplicationId());
			message.append("\nCandidate Name : " + jobApp.getApplicant().getFirstname() + " "
					+ jobApp.getApplicant().getLastname());
			message.append("\n\nBest Regards, \nJob Portal Team");
			emailService.sendMail(jobApp.getJobPosting().getCompany().getEmail(), "Interview Process update",
					message.toString());
			interview.setStatus(status);
		}
		interviewRepository.save(interview);
		return true;
	}

	public void sendInterviewInvitation(Map<String, Object> params) {

		String jobApplicationId = (String) params.get("jobApplicationId");
		String startDate = (String) params.get("startTime");
		String endDate = (String) params.get("endTime");

		JobApplication jobApplication = jobApplicationRepository.findByApplicationId(jobApplicationId);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");

		Date startDateInput = null;
		Date endDateInput = null;
		try {
			startDateInput = formatter.parse(startDate);
			endDateInput = formatter.parse(endDate);
		} catch (ParseException e2) {
			e2.printStackTrace();
		}

		java.util.Calendar startDateInvite = new GregorianCalendar();
		startDateInvite.setTime(startDateInput);
		java.util.Calendar endDateInvite = new GregorianCalendar();
		endDateInvite.setTime(endDateInput);

		String eventName = "Job Interview with " + jobApplication.getApplicant().getFirstname();
		DateTime start = new DateTime(startDateInvite.getTime());
		DateTime end = new DateTime(endDateInvite.getTime());
		VEvent meeting = new VEvent(start, end, eventName);

		UidGenerator ug = null;
		try {
			ug = new UidGenerator("uidGen");
		} catch (SocketException e1) {

			e1.printStackTrace();
		}
		Uid uid = ug.generateUid();
		meeting.getProperties().add(uid);

		net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
		icsCalendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
		icsCalendar.getProperties().add(CalScale.GREGORIAN);
		icsCalendar.getProperties().add(net.fortuna.ical4j.model.property.Version.VERSION_2_0);
		icsCalendar.getComponents().add(meeting);

		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream("./mycalendar.ics");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		CalendarOutputter outputter = new CalendarOutputter();
		try {
			outputter.output(icsCalendar, fout);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ValidationException e) {
			e.printStackTrace();
		}

		MimeMessage message = mailSender.createMimeMessage();
		FileSystemResource file;

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			StringBuilder messageBody = new StringBuilder();

			helper.setTo(jobApplication.getApplicant().getEmail());
			helper.setSubject("Your Interview Schedule for " + jobApplication.getJobPosting().getTitle() + " position");
			messageBody.append("Hi " + jobApplication.getApplicant().getFirstname() + ",\n\n");
			messageBody.append(
					"Find attached interview invite for the position " + jobApplication.getJobPosting().getTitle()
							+ " at " + jobApplication.getJobPosting().getCompany().getCompanyName() + "\n");
			messageBody.append("Login to sjsu job-portal to accept the invite.");
			messageBody.append(
					"\nThis invite is valid for 24 hours from the time of mail sent. Please do make sure to accept it before it expires.\n");
			messageBody.append("\n\nWish you all the very best for the interview.");
			messageBody.append("\n\nBest Regards,");
			messageBody.append(
					"\nTalent Acquisition Team at " + jobApplication.getJobPosting().getCompany().getCompanyName());
			helper.setText(messageBody.toString());
			file = new FileSystemResource("./mycalendar.ics");
			helper.addAttachment(file.getFilename(), file);
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		mailSender.send(message);
	}
}
