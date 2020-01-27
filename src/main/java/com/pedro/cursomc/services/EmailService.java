package com.pedro.cursomc.services;

import com.pedro.cursomc.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);

	void sendEmail(SimpleMailMessage msg);

}
