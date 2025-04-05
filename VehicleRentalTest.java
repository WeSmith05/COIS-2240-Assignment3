import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

class VehicleRentalTest {
	@Test
	public void testLicensePlateValidation() {
		Vehicle car1 = new Car("Toyota", "Corolla", 2019, 5);
        Vehicle car2 = new Car("Honda", "Civic", 2022, 5);
        Vehicle car3 = new Car("Ford", "Focus", 2024, 5);
        
        // Testing Valid Plates
        assertDoesNotThrow(() -> car1.setLicensePlate("AAA100"));
        assertDoesNotThrow(() -> car2.setLicensePlate("ABC567"));
        assertDoesNotThrow(() -> car3.setLicensePlate("ZZZ999"));
        
        // Testing Invalid Plates
        assertThrows(IllegalArgumentException.class, () -> car1.setLicensePlate(""));
        assertThrows(IllegalArgumentException.class, () -> car2.setLicensePlate(null));
        assertThrows(IllegalArgumentException.class, () -> car3.setLicensePlate("AAA1000"));
        assertThrows(IllegalArgumentException.class, () -> car1.setLicensePlate("ZZZ99"));
	}
	
	@Test
	public void testRentAndReturnVehicle() {
		Vehicle car = new Car("Toyota", "Corolla", 2019, 5);
		car.setLicensePlate("AAA111");
		
		Customer customer = new Customer(001, "George");
		RentalSystem system = RentalSystem.getInstance();
		
		// Ensure vehicle is initially available
		assertEquals(Vehicle.VehicleStatus.AVAILABLE, car.getStatus());
		
		// Test rentVehicle() method
		boolean rentSuccessful = system.rentVehicle(car, customer, LocalDate.now(), 500.0);
		assertTrue(rentSuccessful);
		assertEquals(Vehicle.VehicleStatus.RENTED, car.getStatus());
		
		// Rent the same vehicle again and test if it fails
		boolean rentAgain = system.rentVehicle(car, customer, LocalDate.now(), 500.0);
		assertFalse(rentAgain);
		
		// Test returnVehicle method
		boolean returnSuccessful = system.returnVehicle(car, customer, LocalDate.now(), 500.0);
		assertTrue(returnSuccessful);
		assertEquals(Vehicle.VehicleStatus.AVAILABLE, car.getStatus());
		
		// Return the same vehicle again and test if it fails
		boolean returnAgain = system.returnVehicle(car, customer, LocalDate.now(), 500.0);
		assertFalse(returnAgain);
	}
	
	@Test
	public void testSingletonRentalSystem() throws Exception {
		// Return constructor of the RentalSystem class
		Constructor<RentalSystem> constructor = RentalSystem.class.getDeclaredConstructor();
		
		// Get constructors modifiers and validate modifier is private
		int modifiers = constructor.getModifiers();
		assertTrue(Modifier.isPrivate(modifiers));
		
		// Obtain instance of RentalSYstem and assert its not null
		RentalSystem instance = RentalSystem.getInstance();
        assertNotNull(instance);
	}
	
	
	

}
