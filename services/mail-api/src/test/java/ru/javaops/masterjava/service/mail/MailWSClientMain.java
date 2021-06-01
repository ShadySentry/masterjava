package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableSet;

public class MailWSClientMain {
    public static void main(String[] args) {
        MailWSClient.sendToGroup(
                ImmutableSet.of(new Addressee("To <eurohawk@gmail.com>")),
                ImmutableSet.of(new Addressee("Copy <eurohawk1@gmail.com>")), "Subject", "Body");
    }
}