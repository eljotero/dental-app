package com.dentalapp.backend.services;

import com.dentalapp.backend.model.appointment.entity.Appointment;
import com.dentalapp.backend.model.enums.UserType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    public EmailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    @Transactional
    public void sendAccountConfirmationEmail(String to, String name, String link, String language) {
        try {
            String emailText;
            String emailSubject;
            if (language.equals("pl")) {
                emailText = buildAccountConfirmationEmailPl(name, link);
                emailSubject = "Potwierdź swój email";
            } else {
                emailText = buildAccountConfirmationEmailEng(name, link);
                emailSubject = "Confirm your email";
            }
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(emailText, true);
            helper.setTo(to);
            helper.setSubject(emailSubject);
            helper.setFrom("help@denticare.com");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email");
        }
    }

    @Async
    public void sendAppointmentConfirmationEmail(String to, String name, Appointment appointment, String confirmationLink, String language) {
        try {
            String emailText;
            String emailSubject;
            if (language.equals("pl")) {
                emailText = buildAppointmentConfirmationEmailPl(name, appointment, confirmationLink);
                emailSubject = "Potwierdź swoją wizytę";
            } else {
                emailText = buildAppointmentConfirmationEmailEng(name, appointment, confirmationLink);
                emailSubject = "Confirm your appointment";
            }
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(emailText, true);
            helper.setTo(to);
            helper.setSubject(emailSubject);
            helper.setFrom("help@denticare.com");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email");
        }
    }

    @Async
    public void sendAppointmentCancellationEmail(String to, String name, Appointment appointment, String language, String userType) {
        try {
            String emailText;
            String emailSubject;
            if (language.equals("pl")) {
                emailText = buildAppointmentCancellationEmailPl(name, appointment, userType);
                emailSubject = "Anulowanie wizyty";
            } else {
                emailText = buildAppointmentCancellationEmailEng(name, appointment, userType);
                emailSubject = "Appointment Cancellation";
            }
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(emailText, true);
            helper.setTo(to);
            helper.setSubject(emailSubject);
            helper.setFrom("help@denticare.com");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email");
        }
    }

    @Async
    public void sendAppointmentConfirmedEmail(String to, String name, Appointment appointment, String language, String userType) {
        try {
            String emailText;
            String emailSubject;
            if (language.equals("pl")) {
                emailText = buildAppointmentConfirmedEmailPl(name, appointment, userType);
                emailSubject = "Wizyta potwierdzona";
            } else {
                emailText = buildAppointmentConfirmedEmailEn(name, appointment);
                emailSubject = "Appointment confirmed";
            }
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(emailText, true);
            helper.setTo(to);
            helper.setSubject(emailSubject);
            helper.setFrom("help@denticare.com");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email");
        }
    }

    @Async
    public void sendResetPasswordEmail(String to, String name, String link, String language) {
        try {
            String emailText;
            if (language.equals("pl")) {
                emailText = buildResetPasswordEmailPl(name, link);
            } else {
                emailText = buildResetPasswordEmailEng(name, link);
            }
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setText(emailText, true);
            helper.setTo(to);
            helper.setSubject("Reset your password");
            helper.setFrom("help@denticare.com");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email");
        }
    }

    private String buildAppointmentConfirmationEmailEng(String name, Appointment appointment, String confirmationLink) {
        String appointmentDetails = "Appointment with " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName() +
                " on " + appointment.getAppointmentDate() + " at " + appointment.getAppointmentStartTime();
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tbody><tr>\n" +
                "    <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "      <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "        <tbody><tr>\n" +
                "          <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "            <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "              <tbody><tr>\n" +
                "                <td style=\"padding-left:10px\"></td>\n" +
                "                <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                  <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your appointment</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody></table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </tbody></table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "  <tbody><tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Your appointment details are as follows:</p>\n" +
                "      <blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">\n" +
                "        <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + appointmentDetails + "</p>\n" +
                "      </blockquote>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Please confirm your appointment by clicking the link below:</p>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"><a href=\"" + confirmationLink + "\">Confirm Now</a></p>\n" +
                "      <p>See you soon</p>\n" +
                "    </td>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<div class=\"yj6qo\"></div><div class=\"adL\"></div></div>";
    }

    private String buildAppointmentConfirmationEmailPl(String name, Appointment appointment, String confirmationLink) {
        String appointmentDetails = "Wizyta u " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName() +
                " w dniu " + appointment.getAppointmentDate() + " o godzinie " + appointment.getAppointmentStartTime();
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tbody><tr>\n" +
                "    <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "      <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "        <tbody><tr>\n" +
                "          <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "            <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "              <tbody><tr>\n" +
                "                <td style=\"padding-left:10px\"></td>\n" +
                "                <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                  <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Potwierdź swoją wizytę</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody></table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </tbody></table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "  <tbody><tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cześć " + name + ",</p>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Szczegóły Twojej wizyty są następujące:</p>\n" +
                "      <blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">\n" +
                "        <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + appointmentDetails + "</p>\n" +
                "      </blockquote>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Proszę potwierdź swoją wizytę, klikając poniższy link:</p>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"><a href=\"" + confirmationLink + "\">Potwierdź teraz</a></p>\n" +
                "      <p>Do zobaczenia wkrótce</p>\n" +
                "    </td>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<div class=\"yj6qo\"></div><div class=\"adL\"></div></div>";
    }

    private String buildAppointmentConfirmedEmailPl(String name, Appointment appointment, String userType) {
        String appointmentDetails;
        if (UserType.valueOf(userType).equals(UserType.PATIENT)) {
            appointmentDetails = "Wizyta u " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName() +
                    " w dniu " + appointment.getAppointmentDate() + " o godzinie " + appointment.getAppointmentStartTime();
        } else {
            appointmentDetails = "Wizyta pacjenta " + appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName() +
                    " w dniu " + appointment.getAppointmentDate() + " o godzinie " + appointment.getAppointmentStartTime();
        }
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tbody><tr>\n" +
                "    <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "      <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "        <tbody><tr>\n" +
                "          <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "            <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "              <tbody><tr>\n" +
                "                <td style=\"padding-left:10px\"></td>\n" +
                "                <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                  <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Potwierdzenie wizyty</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody></table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </tbody></table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "  <tbody><tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cześć " + name + ",</p>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Twoja wizyta została potwierdzona! Szczegóły Twojej wizyty są następujące:</p>\n" +
                "      <blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">\n" +
                "        <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + appointmentDetails + "</p>\n" +
                "      </blockquote>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Dziękujemy za potwierdzenie wizyty. Do zobaczenia wkrótce!</p>\n" +
                "    </td>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<div class=\"yj6qo\"></div><div class=\"adL\"></div></div>";
    }

    private String buildAppointmentConfirmedEmailEn(String name, Appointment appointment) {
        String appointmentDetails = "Appointment with " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName() +
                " on " + appointment.getAppointmentDate() + " at " + appointment.getAppointmentStartTime();
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tbody><tr>\n" +
                "    <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "      <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "        <tbody><tr>\n" +
                "          <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "            <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "              <tbody><tr>\n" +
                "                <td style=\"padding-left:10px\"></td>\n" +
                "                <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                  <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Appointment Confirmation</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody></table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </tbody></table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "  <tbody><tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Your appointment has been confirmed! The details of your appointment are as follows:</p>\n" +
                "      <blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">\n" +
                "        <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + appointmentDetails + "</p>\n" +
                "      </blockquote>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Thank you for confirming your appointment. See you soon!</p>\n" +
                "    </td>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<div class=\"yj6qo\"></div><div class=\"adL\"></div></div>";
    }

    private String buildAccountConfirmationEmailEng(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    private String buildAccountConfirmationEmailPl(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Potwierdź swój email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cześć " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Dziękujemy za rejestrację. Kliknij poniższy link, aby aktywować swoje konto: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Aktywuj teraz</a> </p></blockquote>\n Link wygaśnie za 15 minut. <p>Do zobaczenia wkrótce</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    private String buildAppointmentCancellationEmailEng(String name, Appointment appointment, String userType) {
        String appointmentDetails;
        if (UserType.valueOf(userType).equals(UserType.PATIENT)) {
            appointmentDetails = "Appointment with " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName() +
                    " on " + appointment.getAppointmentDate() + " at " + appointment.getAppointmentStartTime();
        } else {
            appointmentDetails = "Appointment of patient " + appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName() +
                    " on " + appointment.getAppointmentDate() + " at " + appointment.getAppointmentStartTime();
        }
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tbody><tr>\n" +
                "    <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "      <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "        <tbody><tr>\n" +
                "          <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "            <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "              <tbody><tr>\n" +
                "                <td style=\"padding-left:10px\"></td>\n" +
                "                <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                  <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Appointment Cancellation</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody></table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </tbody></table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "  <tbody><tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">We regret to inform you that your appointment has been cancelled. Here are the details:</p>\n" +
                "      <blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">\n" +
                "        <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + appointmentDetails + "</p>\n" +
                "      </blockquote>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">We apologize for any inconvenience caused. Please contact us if you have any questions.</p>\n" +
                "      <p>Best regards,</p>\n" +
                "      <p>Your Denticare Team</p>\n" +
                "    </td>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<div class=\"yj6qo\"></div><div class=\"adL\"></div></div>";
    }

    private String buildAppointmentCancellationEmailPl(String name, Appointment appointment, String userType) {
        String appointmentDetails;
        if (UserType.valueOf(userType).equals(UserType.PATIENT)) {
            appointmentDetails = "Wizyta u " + appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName() +
                    " w dniu " + appointment.getAppointmentDate() + " o godzinie " + appointment.getAppointmentStartTime();
        } else {
            appointmentDetails = "Wizyta pacjenta " + appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName() +
                    " w dniu " + appointment.getAppointmentDate() + " o godzinie " + appointment.getAppointmentStartTime();
        }
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tbody><tr>\n" +
                "    <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "      <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "        <tbody><tr>\n" +
                "          <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "            <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "              <tbody><tr>\n" +
                "                <td style=\"padding-left:10px\"></td>\n" +
                "                <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                  <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Anulowanie wizyty</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody></table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </tbody></table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "  <tbody><tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cześć " + name + ",</p>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Z przykrością informujemy, że Twoja wizyta została anulowana. Oto szczegóły:</p>\n" +
                "      <blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">\n" +
                "        <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + appointmentDetails + "</p>\n" +
                "      </blockquote>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Przepraszamy za wszelkie niedogodności. Prosimy o kontakt, jeśli masz jakiekolwiek pytania.</p>\n" +
                "      <p>Z poważaniem,</p>\n" +
                "      <p>Zespół Denticare</p>\n" +
                "    </td>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<div class=\"yj6qo\"></div><div class=\"adL\"></div></div>";
    }

    private String buildResetPasswordEmailEng(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tbody><tr>\n" +
                "    <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "      <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "        <tbody><tr>\n" +
                "          <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "            <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "              <tbody><tr>\n" +
                "                <td style=\"padding-left:10px\"></td>\n" +
                "                <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                  <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Reset your password</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody></table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </tbody></table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "  <tbody><tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">We received a request to reset your password. Click the link below to reset it:</p>\n" +
                "      <blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">\n" +
                "        <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"><a href=\"" + link + "\">Reset Password</a></p>\n" +
                "      </blockquote>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">If you did not request a password reset, please ignore this email.</p>\n" +
                "      <p>Thank you,</p>\n" +
                "      <p>Your Denticare Team</p>\n" +
                "    </td>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<div class=\"yj6qo\"></div><div class=\"adL\"></div></div>";
    }

    private String buildResetPasswordEmailPl(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tbody><tr>\n" +
                "    <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "      <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "        <tbody><tr>\n" +
                "          <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "            <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "              <tbody><tr>\n" +
                "                <td style=\"padding-left:10px\"></td>\n" +
                "                <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                  <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Zresetuj swoje hasło</span>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody></table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "      </tbody></table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<table role=\"presentation\" class=\"content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "  <tbody><tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Cześć " + name + ",</p>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Otrzymaliśmy prośbę o zresetowanie hasła. Kliknij poniższy link, aby je zresetować:</p>\n" +
                "      <blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">\n" +
                "        <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"><a href=\"" + link + "\">Zresetuj hasło</a></p>\n" +
                "      </blockquote>\n" +
                "      <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Jeśli nie prosiłeś o zresetowanie hasła, zignoruj tę wiadomość.</p>\n" +
                "      <p>Dziękujemy,</p>\n" +
                "      <p>Zespół Denticare</p>\n" +
                "    </td>\n" +
                "    <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td height=\"30\"><br></td>\n" +
                "  </tr>\n" +
                "</tbody></table>\n" +
                "<div class=\"yj6qo\"></div><div class=\"adL\"></div></div>";
    }


}
