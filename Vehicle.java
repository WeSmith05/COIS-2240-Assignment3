public abstract class Vehicle {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus { AVAILABLE, RESERVED, RENTED, MAINTENANCE, OUTOFSERVICE }

    public Vehicle(String make, String model, int year) {
    	// Simplified using helper method capitalize().
		this.make =	capitalize(make);
    	this.model = capitalize(model);
    	
        this.year = year;
        this.status = VehicleStatus.AVAILABLE;
        this.licensePlate = null;
    }

    // Helper method capitalize() that capitalizes the first letter and makes the rest lowercase.
	private String capitalize(String input) {
		if (input == null || input.isEmpty()) {
			return null;
		}
		return input.substring(0, 1).toUpperCase() + model.substring(1).toLowerCase();
	}

    public Vehicle() {
        this(null, null, 0);
    }

    public void setLicensePlate(String plate) {
        this.licensePlate = plate == null ? null : plate.toUpperCase();
    }

    public void setStatus(VehicleStatus status) {
    	this.status = status;
    }

    public String getLicensePlate() { return licensePlate; }

    public String getMake() { return make; }

    public String getModel() { return model;}

    public int getYear() { return year; }

    public VehicleStatus getStatus() { return status; }

    public String getInfo() {
        return "| " + licensePlate + " | " + make + " | " + model + " | " + year + " | " + status + " |";
    }

}
