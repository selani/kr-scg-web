package com.mkyong.core;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBExample {

	public static void main(String[] args) {
		test1(args);
		test2(args);
	}

	public static void test1(String[] args) {

		Customer customer = new Customer();
		customer.setId(100);
		customer.setName("mkyong");
		customer.setAge(29);

		try {

			File file = new File("C:\\file.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(customer, file);
			jaxbMarshaller.marshal(customer, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static void test2(String[] args) {

		try {

			File file = new File("C:\\file.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Customer customer = (Customer) jaxbUnmarshaller.unmarshal(file);
			System.out.println(customer);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}