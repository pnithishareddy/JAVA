
package HOSPITAL;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

class HospitalException extends Exception {
HospitalException(String message) {
super(message);
}
}

abstract class Person {
private int id;
private String name;

Person(int id, String name) {
this.id = id;
this.name = name;
}

public int getId() {
return id;
}

public String getName() {
return name;
}

abstract void display();
}

class Patient extends Person {
ArrayList<String> history = new ArrayList<>();

Patient(int id, String name) {
super(id, name);
}

void display() {
System.out.println("Patient ID: " + getId());
System.out.println("Patient Name: " + getName());
}
}

class Doctor extends Person {
ArrayList<LocalTime> slots = new ArrayList<>();

Doctor(int id, String name) {
super(id, name);

slots.add(LocalTime.of(9, 0));
slots.add(LocalTime.of(10, 0));
slots.add(LocalTime.of(11, 0));
slots.add(LocalTime.of(12, 0));
slots.add(LocalTime.of(14, 0));
slots.add(LocalTime.of(15, 0));
}

void display() {
System.out.println("Doctor ID: " + getId());
System.out.println("Doctor Name: " + getName());
}
}

class Appointment {
String id;
int patientId;
int doctorId;
LocalDate date;
LocalTime time;
boolean cancelled;

Appointment(String id, int patientId, int doctorId, LocalDate date, LocalTime time) {
this.id = id;
this.patientId = patientId;
this.doctorId = doctorId;
this.date = date;
this.time = time;
this.cancelled = false;
}

public String toString() {
return id + " | Patient: " + patientId + " | Doctor: " + doctorId + " | Date: " + date + " | Time: " + time + (cancelled ? " | CANCELLED" : " | BOOKED");
}
}

class Hospital {
Map<Integer, Patient> patients = new HashMap<>();
Map<Integer, Doctor> doctors = new HashMap<>();
Map<String, Appointment> appointments = new HashMap<>();

int patientId = 1;
int doctorId = 1;

Patient registerPatient(String name) {
Patient p = new Patient(patientId++, name);
patients.put(p.getId(), p);
return p;
}

Doctor addDoctor(String name) {
Doctor d = new Doctor(doctorId++, name);
doctors.put(d.getId(), d);
return d;
}

Appointment bookAppointment(int pId, int dId, LocalDate date, LocalTime time) throws HospitalException {
if (!patients.containsKey(pId)) {
throw new HospitalException("Patient not found");
}

Doctor doctor = doctors.get(dId);

if (doctor == null) {
throw new HospitalException("Doctor not found");
}

if (!doctor.slots.contains(time)) {
throw new HospitalException("Invalid time slot");
}

String key = dId + "_" + date + "_" + time;

if (appointments.containsKey(key) && !appointments.get(key).cancelled) {
throw new HospitalException("Slot already booked");
}

Appointment a = new Appointment(key, pId, dId, date, time);

appointments.put(key, a);
patients.get(pId).history.add(key);

return a;
}

void cancelAppointment(String appointmentId) throws HospitalException {
Appointment a = appointments.get(appointmentId);

if (a == null || a.cancelled) {
throw new HospitalException("Appointment not found");
}

a.cancelled = true;

System.out.println("Appointment cancelled successfully");
}

void availableSlots(int dId, LocalDate date) throws HospitalException {
Doctor doctor = doctors.get(dId);

if (doctor == null) {
throw new HospitalException("Doctor not found");
}

ArrayList<LocalTime> free = new ArrayList<>(doctor.slots);

for (Appointment a : appointments.values()) {
if (a.doctorId == dId && a.date.equals(date) && !a.cancelled) {
free.remove(a.time);
}
}

System.out.println("Available Slots: " + free);
}

void doctorSchedule(int dId) throws HospitalException {
Doctor doctor = doctors.get(dId);

if (doctor == null) {
throw new HospitalException("Doctor not found");
}

System.out.println("\nDoctor: " + doctor.getName());
System.out.println("Doctor Schedule:");

boolean found = false;

for (Appointment a : appointments.values()) {
if (a.doctorId == dId && !a.cancelled) {
System.out.println(a);
found = true;
}
}

if (!found) {
System.out.println("No appointments booked for this doctor.");
}
}

void patientHistory(int pId) throws HospitalException {
Patient p = patients.get(pId);

if (p == null) {
throw new HospitalException("Patient not found");
}

System.out.println("\nPatient: " + p.getName());
System.out.println("Patient History:");

if (p.history.isEmpty()) {
System.out.println("No appointments found.");
return;
}

for (String id : p.history) {
System.out.println(appointments.get(id));
}
}

void viewAllPatients() {
System.out.println("\n===== ALL REGISTERED PATIENTS =====");

if (patients.isEmpty()) {
System.out.println("No patients registered.");
return;
}

for (Patient p : patients.values()) {
p.display();
System.out.println();
}
}

void viewAllDoctors() {
System.out.println("\n===== ALL REGISTERED DOCTORS =====");

if (doctors.isEmpty()) {
System.out.println("No doctors registered.");
return;
}

for (Doctor d : doctors.values()) {
d.display();
System.out.println();
}
}
}

public class HOSPITALL {
public static void main(String[] args) {

Scanner sc = new Scanner(System.in);
Hospital hospital = new Hospital();

while (true) {

System.out.println("\n===== HOSPITAL APPOINTMENT SYSTEM =====");
System.out.println("1. Register Patient");
System.out.println("2. Add Doctor");
System.out.println("3. Book Appointment");
System.out.println("4. Cancel Appointment");
System.out.println("5. View Available Slots");
System.out.println("6. View Doctor Schedule");
System.out.println("7. View Patient History");
System.out.println("8. View All Patients");
System.out.println("9. View All Doctors");
System.out.println("10. Exit");

System.out.print("Enter your choice: ");
int choice = sc.nextInt();
sc.nextLine();

try {

switch (choice) {

case 1:
System.out.print("Enter patient name: ");
String patientName = sc.nextLine();

Patient p = hospital.registerPatient(patientName);

System.out.println("Patient registered successfully");
System.out.println("Patient ID: " + p.getId());
break;

case 2:
System.out.print("Enter doctor name: ");
String doctorName = sc.nextLine();

Doctor d = hospital.addDoctor(doctorName);

System.out.println("Doctor added successfully");
System.out.println("Doctor ID: " + d.getId());
break;

case 3:
System.out.print("Enter patient ID: ");
int pId = sc.nextInt();

System.out.print("Enter doctor ID: ");
int dId = sc.nextInt();

sc.nextLine();

System.out.print("Enter date (YYYY-MM-DD): ");
LocalDate date = LocalDate.parse(sc.nextLine());

System.out.println("\nAvailable time slots:");
System.out.println("09:00");
System.out.println("10:00");
System.out.println("11:00");
System.out.println("12:00");
System.out.println("14:00");
System.out.println("15:00");

System.out.print("Enter time (HH:MM): ");
LocalTime time = LocalTime.parse(sc.nextLine());

Appointment a = hospital.bookAppointment(pId, dId, date, time);

System.out.println("\nAppointment booked successfully");
System.out.println(a);
break;

case 4:
System.out.print("Enter appointment ID: ");
String appointmentId = sc.nextLine();

hospital.cancelAppointment(appointmentId);
break;

case 5:
System.out.print("Enter doctor ID: ");
int availableDoctor = sc.nextInt();

sc.nextLine();

System.out.print("Enter date (YYYY-MM-DD): ");
LocalDate availableDate = LocalDate.parse(sc.nextLine());

hospital.availableSlots(availableDoctor, availableDate);
break;

case 6:
System.out.print("Enter doctor ID: ");
int scheduleDoctor = sc.nextInt();

hospital.doctorSchedule(scheduleDoctor);
break;

case 7:
System.out.print("Enter patient ID: ");
int historyPatient = sc.nextInt();

hospital.patientHistory(historyPatient);
break;

case 8:
hospital.viewAllPatients();
break;

case 9:
hospital.viewAllDoctors();
break;

case 10:
System.out.println("Thank you for using Hospital Appointment System.");
sc.close();
return;

default:
System.out.println("Invalid choice");
}

}
catch (HospitalException e) {
System.out.println("Error: " + e.getMessage());
}
catch (Exception e) {
System.out.println("Invalid input");
sc.nextLine();
}
}
}
}
