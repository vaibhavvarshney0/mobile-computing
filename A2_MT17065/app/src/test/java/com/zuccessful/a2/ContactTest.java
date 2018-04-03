package com.zuccessful.a2;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContactTest {

    Contact test_contact;

    @Before
    public void setUp() throws Exception {
        test_contact = new Contact("test_id", "test_name", "null");
        test_contact.setNumber("123");
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void getEmail() {
    }

    @Test
    public void setEmail() {
    }

    @Test
    public void setNumber() {
        test_contact.setNumber("123");
    }

    @Test
    public void getNumber() {
        assertNotNull(test_contact.getNumber());
    }

    @Test
    public void getId() {
        assertNotNull(test_contact.getId());
    }

    @Test
    public void setId() {
    }

    @Test
    public void getTitle() {
        assertNotNull(test_contact.getTitle());
    }

    @Test
    public void setTitle() {
    }

    @Test
    public void getImage() {
    }

    @Test
    public void setImage() {
    }
}