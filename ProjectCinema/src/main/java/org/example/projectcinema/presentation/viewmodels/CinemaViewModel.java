package org.example.projectcinema.presentation.viewmodels;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;


public class CinemaViewModel {

    @NotBlank(message = "Cinema name is required.")
    private String name;

    @Positive(message = "Capacity must be a positive number.")
    private int capacity;

    @NotBlank(message = "Address is required.")
    private String address;

    @NotBlank(message = "Image name is required.")
    private String image;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}

