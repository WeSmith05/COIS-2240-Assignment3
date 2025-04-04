import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class RentalSystem {
	private static RentalSystem instance = null;
	
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    
    //Private constructor
    private RentalSystem() {}

    
    // getInstance() class to provide access to single instance
    public static RentalSystem getInstance() {
    	if (instance == null) {
    		instance = new RentalSystem();
    	} 
    	return instance;
    }
    
    // New methods to save vehicle, customer, and rental record details even after exiting program.
    
    // saveVehicle method to save vehicle object to vehicles.txt
    private void saveVehicle(Vehicle vehicle) {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt", true))) {
    		writer.write(vehicle.getInfo()); 
    		writer.newLine();
    	} catch (IOException e) {
    		System.err.println("Error saving vehicle: " + e.getMessage());
    	}
    }
    
    // saveCustomer method to save customer object to customers.txt
    private void saveCustomer(Customer customer) {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
    		writer.write(customer.toString());
    		writer.newLine();
    	} catch (IOException e ) {
    		System.err.println("Error saving customer: " + e.getMessage());
    	}
    }
    
    // saveRecord method to save RentalRecord object to rental_records.txt
    private void saveRecord(RentalRecord record) {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter("rental_records.txt", true))) {
    		writer.write(record.toString());
    		writer.newLine();
    	} catch (IOException e ) {
    		System.err.println("Error saving rental record: " + e.getMessage());
    	}
    }
    
    
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        saveVehicle(vehicle);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        saveCustomer(customer);
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setStatus(Vehicle.VehicleStatus.RENTED);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            saveRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            saveRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    public void displayVehicles(boolean onlyAvailable) {
    	System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
    	System.out.println("---------------------------------------------------------------------------------");
    	 
        for (Vehicle v : vehicles) {
            if (!onlyAvailable || v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
            }
        }
        System.out.println();
    }
    
    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        for (RentalRecord record : rentalHistory.getRentalHistory()) {
            System.out.println(record.toString());
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(String id) {
        for (Customer c : customers)
            if (c.getCustomerId() == Integer.parseInt(id))
                return c;
        return null;
    }
}