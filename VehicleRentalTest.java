import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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

}
