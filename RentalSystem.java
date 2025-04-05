import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
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
    private RentalSystem() {
    	loadData();
    }

    
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
    		writer.write(vehicle.getClass().getSimpleName() + vehicle.getInfo()); 
    		writer.newLine();
    	} catch (IOException e) {
    		System.err.println("Error saving vehicle: " + e.getMessage());
    	}
    }
    
    // saveCustomer method to save customer object to customers.txt
    private void saveCustomer(Customer customer) {
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
    		writer.write(
    				customer.getCustomerId() + "," +
    				customer.getCustomerName());
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
    
   
    public boolean addVehicle(Vehicle vehicle) {
    	// Check if the license plate already exists
    	if (findVehicleByPlate(vehicle.getLicensePlate()) != null) {
    		System.out.println("A vehicle with this license plate already exists");
    		return false;
    	}
    	
        vehicles.add(vehicle);
        saveVehicle(vehicle);
        return true;
    }

    public boolean addCustomer(Customer customer) {
    	// Check if the customer ID already exists
    	if (findCustomerById(String.valueOf(customer.getCustomerId())) != null ) {
    		System.out.println("A customer with this custoemr ID already exists");
    		return false;
    	}
    	
        customers.add(customer);
        saveCustomer(customer);
        return true;
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
    
    // Method to load all of the saved data from their .txt files
    private void loadData() {
    	loadVehicles();
    	loadCustomers();
    	loadRecords();
    }
    
    // Method to load vehicles from vehicles.txt
    private void loadVehicles() {
    	try (BufferedReader reader = new BufferedReader(new FileReader("vehicles.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	line = line.replace(" ", "");
                String[] parts = line.split("\\|");
                
                String type = parts[0];
                String plate = parts[1];
                String make = parts[2];
                String model = parts[3];
                int year = Integer.parseInt(parts[4]);
                Vehicle.VehicleStatus status = Vehicle.VehicleStatus.valueOf(parts[5]);

                Vehicle vehicle = null;
                switch (type) {
	                case "Car":
	                	int numSeats = Integer.parseInt(parts[6].split(":")[1].trim());
	                    vehicle = new Car(make, model, year, numSeats);
	                    break;
	                case "Motorcycle":
                    	boolean hasSidecar = parts[6].split(":")[1].trim().equalsIgnoreCase("Yes");
                        vehicle = new Motorcycle(make, model, year, hasSidecar);
                        break;
                    case "SportCar":
                    	int numSeatsSport = Integer.parseInt(parts[6].split(":")[1].trim());
                    	int horsepower = Integer.parseInt(parts[7].split(":")[1].trim());
                    	boolean hasTurbo = parts[8].split(":")[1].trim().equalsIgnoreCase("Yes");
                        vehicle = new SportCar(make, model, year, numSeatsSport, horsepower, hasTurbo);
                        break;
                    case "Truck":
                        double cargoCapacity = Double.parseDouble(parts[6].split(":")[1].trim());
                        vehicle = new Truck(make, model, year, cargoCapacity);
                        break;
                }

                if (vehicle != null) {
                	vehicle.setLicensePlate(plate);
                	vehicle.setStatus(status);
                    vehicles.add(vehicle);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading vehicles: " + e.getMessage());
        }
    }
    
    // Method to load customers from customer.txt
    private void loadCustomers() {
    	try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
    		String line;
    		while ((line = reader.readLine()) != null) {
    			String[] parts = line.split(",");
    			
    			int customerId = Integer.parseInt(parts[0]);
    			String customerName = parts[1];
    			
    			Customer customer = new Customer(customerId, customerName);
    			
    			customers.add(customer);
    		}
    	} catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
    }
    
    // Method to load rental records from rental_records.txt
    private void loadRecords() {
    	try (BufferedReader reader = new BufferedReader(new FileReader("rental_records.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
            	line = line.replace(" ", "");
                String[] parts = line.split("\\|");
                
                String recordType = parts[0];
                String licensePlate = parts[1].split(":")[1].trim();
                String customerName = parts[2].split(":")[1].trim();
                LocalDate recordDate = LocalDate.parse(parts[3].split(":")[1].trim());
                double totalAmount = Double.parseDouble(parts[4].split(":")[1].trim().replace("$", "").trim());
                
                Vehicle vehicle = findVehicleByPlate(licensePlate);
                Customer customer = findCustomerById(customerName);
                
                if (vehicle != null && customer != null) {
                    RentalRecord rentalRecord = new RentalRecord(vehicle, customer, recordDate, totalAmount, recordType);
                    rentalHistory.addRecord(rentalRecord);
                }
                
            }
    	} catch (IOException e) {
            System.err.println("Error loading rental records: " + e.getMessage());
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