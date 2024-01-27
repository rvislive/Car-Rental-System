import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Scanner;
class Car {
    private String carId;
    private String model;
    private String brand;
    private double basePricePerDay;
    private boolean isAvailable;

    public Car(String carId, String model, String brand, double basePricePerDay) {
        this.carId = carId;
        this.model = model;
        this.brand = brand;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getCarId() {
        return carId;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public double getBasePricePerDay() {
        return basePricePerDay;
    }

    public double calculatePrice(int rentDays) {
        return basePricePerDay * rentDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}
class Customer {
    private String customerId;
    private String name;
    private String phoneNumber;

    public Customer(String customerId, String name, String phoneNumber) {
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
class Rental {

    private int bookingId;
    private Car car;
    private Customer customer;
    private int days;

    public Rental(int bookingId, Car car, Customer customer, int days) {
        this.bookingId = bookingId;
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public int getBookingId() {
        return bookingId;
    }
    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}
class CarRentalSystem {
    private ArrayList<Car> cars;
    private ArrayList<Customer> customers;
    private ArrayList<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(int bookingId, Car car, Customer customer, int day) {
        if(car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(bookingId, car, customer, day));
        } else {
            System.out.println("Ooh " + customer.getName() + "! This car is not available.");
        }
    }

    public void returnCar(Car car) {
        Rental returnedCar = null;
        for(Rental rental: rentals) {
            if(rental.getCar() == car) {
                returnedCar = rental;
                break;
            }
        }

        if(returnedCar != null) {
            car.returnCar();
            rentals.remove(returnedCar);
        } else {
            System.out.println("This car(Id: " + car.getCarId() +") was not rented.");
        }

    }

    public void menu() {
        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("--------- Welcome to Car Rental System 🙏 -------");
            System.out.println("1. New Customer? Register yourself.");
            System.out.println("2. New Car? Register your car.");
            System.out.println("3. Rent a car? Just few clicks ahead.");
            System.out.println("4. Return my rented Car!");
            System.out.println("5. Exit!");
            System.out.println("Please select an options.");

            int choice = sc.nextInt();
            sc.nextLine();

            switch(choice) {
                case 1:
                    System.out.println("1. May I please know your name & phone number?");
                    String customerId = "CUS00" + (customers.size() + 1);
                    String customerName = sc.nextLine();
                    String phoneNumber = sc.nextLine();
                    Customer newCustomer = new Customer(customerId, customerName, phoneNumber);
                    addCustomer(newCustomer);
                    System.out.println("Customer Added!");
                    break;
                case 2: {
                    System.out.println("2. Please enter your car model, brand and basePricePerDay?");
                    String carId = "00" + (cars.size() + 1);
                    String model = sc.nextLine();
                    String branch = sc.nextLine();
                    double basePricePerDay = sc.nextDouble();
                    Car newCar = new Car(carId, model, branch, basePricePerDay);
                    addCar(newCar);
                    System.out.println("Car Added!");
                    break;
                }
                case 3: {
                    System.out.println("3. Rent a car? Here are the few details of all available cars.");

                    for(Car car: cars) {
                        if(car.isAvailable()) {
                            System.out.println(car.getCarId() + "  " + car.getBrand() + "  " + car.getModel() + "   Rs." + car.getBasePricePerDay() + "/days.");
                        }
                    }

                    System.out.print("Please select the Car ID to book. :)");
                    String carId = sc.nextLine();

                    Car selectedCar = null;
                    for(Car car: cars) {
                        if(car.getCarId().equals(carId) && car.isAvailable()) {
                            selectedCar = car;
                            break;
                        }
                    }

                    if(selectedCar != null) {
                        System.out.print("Amazing Choice, you have selected " + selectedCar.getBrand() + ", " + selectedCar.getModel());
                        System.out.println(" Can you verify your customer Id?");
                        String cusId = sc.nextLine();

                        Customer selectedCustomer = null;
                        for(Customer customer: customers) {
                            if (customer.getCustomerId().equals(cusId)) {
                                System.out.println("Account Verified!");
                                selectedCustomer = customer;
                                break;
                            }
                        }

                        if(selectedCustomer != null) {
                            System.out.println("Booking for how many days?");
                            int days = sc.nextInt();

                            System.out.println("Here is your booking details: Please confirm(Y/N)?");
                            int bookingId = rentals.size() + 1;

                            System.out.println("Booking Details: "+ bookingId + " " + selectedCar.getBrand() + " " + selectedCar.getModel() + " " + selectedCar.getBasePricePerDay() + " for " + days + "days.");

                            char confirmation = sc.next().charAt(0);
                            if(confirmation == 'Y') {
                                rentCar(bookingId, selectedCar, selectedCustomer, days);
                                System.out.println("Thank you so much " + selectedCustomer.getName() + ", for using our services, you booking id is: " + bookingId + " amount to be paid is:" + selectedCar.calculatePrice(days));
                            } else {
                                System.out.println("Booking Cancelled.");
                            }
                        } else {
                            System.out.print("Provided Customer detail is not registered.");
                        }
                    } else {
                        System.out.print("Provided Car details are either not registered or not available.");
                    }
                    break;
                }
                case 4: {
                    System.out.println("4. Return my rented Car!, Please let me know your booking Id?");
                    int bookingId = sc.nextInt();

                    Rental returnRentalCar = null;
                    for(Rental rental: rentals) {
                        if(rental.getBookingId() == bookingId) {
                            returnRentalCar = rental;
                            break;
                        }
                    }

                    if(returnRentalCar != null) {
                        returnCar(returnRentalCar.getCar());
                    } else {
                        System.out.print("Invalid Booking Id.");
                    }

                    System.out.println("your car returned.");
                    break;
                }
                case 5:
                    break;
                default:
                    System.out.println("Please enter an valid options.");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Camry", 60.0); // Different base price per day for each car
        Car car2 = new Car("C002", "Honda", "Accord", 70.0);
        Car car3 = new Car("C003", "Mahindra", "Thar", 150.0);
        Customer cus1 = new Customer("CUS001", "Rajan", "965-454-5646");
        rentalSystem.addCustomer(cus1);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        rentalSystem.menu();
    }
}