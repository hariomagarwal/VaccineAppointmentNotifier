package com.notifier;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.client.RestTemplate;

import com.notifier.dto.CenterDTO;
import com.notifier.dto.CentersDTO;
import com.notifier.dto.SessionDTO;

@SpringBootApplication
public class AppointmentNotifier implements CommandLineRunner {

	@Autowired
	private Environment env;
	
	private static final Log LOGGER = LogFactory.getLog(AppointmentNotifier.class);
	@Autowired
	private JavaMailSender mailSender;
	public static void main(String[] args) {
		SpringApplication.run(AppointmentNotifier.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		getAppointmentDetails();
	}
	
	public void getAppointmentDetails() throws Exception {
		LOGGER.info("Here");
		LocalDateTime now = LocalDateTime.now();
		String date = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(now);
		LOGGER.info("-------------");
		LOGGER.info(date);
		// 654 - gorakhpur , 276-bangalore rural, 265 - bangalore urban
		String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="+env.getProperty("districtId")+"&date="+date;
		RestTemplate restTemplate = new RestTemplate();
		CentersDTO centersDTO = restTemplate.getForObject(url, CentersDTO.class);
		List<CenterDTO> centerDTOList =  centersDTO.getCenters();
		List<CenterDTO> finalCenterDTOList = new ArrayList<>();
		
		for (CenterDTO centerDTO : centerDTOList) {
			LOGGER.info("----Center DTO-----");
			LOGGER.info(centerDTO);
			List<SessionDTO> sessionDTOList = centerDTO.getSessions();
			List<SessionDTO> finalSessionDTOList = new ArrayList<>();
			for (SessionDTO sessionDTO : sessionDTOList) {
				if (sessionDTO.getMin_age_limit() == 18 && sessionDTO.getAvailable_capacity() > 0) {
					LOGGER.info("----Above-----");
					LOGGER.info(sessionDTO);
					finalSessionDTOList.add(sessionDTO);
				}
			}
			if (!finalSessionDTOList.isEmpty()) {
				CenterDTO centerDTObj = new CenterDTO();
				centerDTObj.setCenter_id(centerDTO.getCenter_id());
				centerDTObj.setName(centerDTO.getName());
				centerDTObj.setAddress(centerDTO.getAddress());
				centerDTObj.setState_name(centerDTO.getState_name());
				centerDTObj.setDistrict_name(centerDTO.getDistrict_name());
				centerDTObj.setBlock_name(centerDTO.getBlock_name());
				centerDTObj.setPincode(centerDTO.getPincode());
				centerDTObj.setFee_type(centerDTO.getFee_type());
				centerDTObj.setSessions(finalSessionDTOList);
				finalCenterDTOList.add(centerDTObj);
			}
		}
			
		if (!finalCenterDTOList.isEmpty()) {
			SendEmail(finalCenterDTOList);
			Thread.sleep(3600000);
			getAppointmentDetails();
		}
		else {
			LOGGER.info("wait 5 min");
			Thread.sleep(300000);
			getAppointmentDetails();
		}
		
	}
	
	public void SendEmail(List<CenterDTO> finalCenterDTOList) throws MessagingException, UnsupportedEncodingException {
		String body = "";
		for (CenterDTO centerDTO : finalCenterDTOList) {
			body = body + "<br><b>Name:</b> &nbsp;&nbsp; "+centerDTO.getName()+"<br><b>Address:</b>&nbsp;&nbsp; "+centerDTO.getAddress()+"<br><b>State Name:</b>&nbsp;&nbsp; "+centerDTO.getState_name()+"<br><b>District Name:</b> &nbsp;&nbsp;"+centerDTO.getDistrict_name()+"<br><b>Bloack Name:</b>&nbsp;&nbsp; "+centerDTO.getBlock_name()+"<br><b>Pin code:</b> &nbsp;&nbsp;"+centerDTO.getPincode()+"<br><b>Fee Type:</b>&nbsp;&nbsp;"+centerDTO.getFee_type();
			body = body+"<br>";
			List<SessionDTO> sessionDTOList = centerDTO.getSessions();
			body += "<table  border=\"1\"><tr><th>Date</th><th>Available Capacity</th><th>Min Age Limit</th><th>Vaccine</th></tr>";
			for (SessionDTO sessionDTO : sessionDTOList) {
				body += "<tr><td>"+sessionDTO.getDate()+"</td><td>"+sessionDTO.getAvailable_capacity()+"</td><td>"+sessionDTO.getMin_age_limit()+"</td><td>"+sessionDTO.getVaccine()+"</td></tr>";
			}
			body += "</table>";
			body+= "<br>";
			body+= "-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------<br>";
		}
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
		LocalDateTime now = LocalDateTime.now();
		helper.setFrom(new InternetAddress(env.getProperty("spring.mail.username"), "Vaccine Appointment Notifier"));
		helper.setTo(env.getProperty("receiverMailId"));
		helper.setText(body, true);
		helper.setSubject("Appointment Available!!!"+ now.toString());
		mailSender.send(mimeMessage);
		
	}

}