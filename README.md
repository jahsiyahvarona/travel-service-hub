## Title
> TSH (Travel Services Hub)

## Team Members
> Jahsiyah Varona, Saniyah Khan, Shaka Ombongi


## Description
> Travel Services Hub is a user-friendly web app designed to connect travelers with expert travel agents who offer curated travel packages. Our users will have access to a >
> platform that allows them to search for travel packages by destination, view agent profiles, and read reviews from other customers.
>
> Travel agents (service providers) will create and manage travel packages for various destinations, offering customers a wide range of options to choose from.
>
> Customers will be able to browse through these packages, make bookings for their next vacation, and leave reviews based on their experiences.
>
> SysAdmins will monitor the platform to ensure the integrity of the content. They will be able to approve travel agent profiles, flag or delete inappropriate reviews, and >
> manage the platform's content.
make this all in the formate for readme. do it in one code block like markdown: # **Travel Services Hub**


## **Features**
> **Providers**
>  - Create and modify profiles
>  - Add and manage services with searchable criteria
>  - Reply to customer reviews
>  - View customer engagement statistics
>
 **Customers**
>  - Create and modify profiles
>  - Book services
>  - Write, edit, and view reviews

**SysAdmins**
>  - View platform usage statistics
> - Moderate reviews by deleting inappropriate content
> - Manage user accounts, including banning users
>
### **Prerequisites**
> - **Java 11** or higher
> - **Maven**
> - **MySQL Database**

### **Steps to Run the Project**
> **Clone the Repository**
>   
>bash
>   git clone [https://github.com/jahsiyahvarona/travel-service-hub.git](https://github.com/jahsiyahvarona/travel-service-hub.git)
>   cd travel-services-hub
>Import Database
>
>Use the provided ArchiveData.sql file to set up the initial database schema and data.
>Execute the SQL script using your preferred MySQL client:
>
## **Configure Application Properties**
>Update the src/main/resources/application.properties file with your MySQL database credentials:
>
>**properties**
>spring.datasource.url=jdbc:mysql://localhost:3306/travel_services_hub
>spring.datasource.username=yourusername
>spring.datasource.password=yourpassword
>Build and Run the Application

## **testing**
>
>Open your browser and navigate to [http://localhost:8080.](http://localhost:8080/welcome).
>
> **Default Accounts**
> - Provider
>   - Username: provider1
>   - Password: 12345678
>    
> - Customer
>   - Username: customer1
>   - Password: 12345678

## **Usage**
> **Provider Actions**
> - Log in using the Provider account.
> - Create or modify your profile.
> - Add services with specific searchable criteria.
> - View and reply to customer reviews.
> - Access customer statistics to monitor engagement.
>
> **Customer Actions**
> - Log in using the Customer account.
> - Create or modify your profile.
> - Browse and book services from providers.
> - Write reviews for services you've booked.
> - View existing reviews for transparency.
