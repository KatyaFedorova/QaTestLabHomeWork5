package myprojects.automation.assignment5.model;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private Address address;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email.replace("@", "+@" + System.currentTimeMillis());
    }

    public Address getAddress() {
        return address;
    }

    public class Address extends User {
        private String postCode;
        private String city;
        private String street;
        private String house;
        private String apartment;

        public String getPostCode() {
            return postCode;
        }

        public String getCity() {
            return city;
        }

        public String getStreet() {
            return street;
        }

        public String getHouse() {
            return house;
        }

        public String getApartment() {
            return apartment;
        }
    }
}
