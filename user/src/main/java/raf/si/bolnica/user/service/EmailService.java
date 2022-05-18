package raf.si.bolnica.user.service;

import raf.si.bolnica.user.models.User;

public interface EmailService {

    boolean sendEmail(String usersEmail, String password);
}
