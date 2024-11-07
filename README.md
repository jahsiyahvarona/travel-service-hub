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

Provider Profile Endpoints
POST http://localhost:8080/providers - Create a new provider profile.
Body:
json
{
"username": "provider123",
"password": "securePassword123",
"email": "provider@example.com",
"profilePic": "http://example.com/image.png"
}

GET http://localhost:8080/providers/{providerId} - Retrieve a provider profile by ID.
Path Variable: {providerId} - Use the ID of an existing provider.


PUT http://localhost:8080/providers/{providerId} - Update a provider profile.
Path Variable: {providerId} - Use the ID of an existing provider.
Body:
json
{
"username": "updatedProvider123",
"email": "updated@example.com",
"profilePic": "http://example.com/newimage.png"
}


DELETE http://localhost:8080/providers/{providerId} - Deactivate a provider profile.
Path Variable: {providerId} - Use the ID of an existing provider.

Package Management Endpoints
POST http://localhost:8080/providers/{providerId}/packages - Create a new package for a provider.
Path Variable: {providerId} - Use the ID of an existing provider.
Body:
json

{
"name": "Adventure Package",
"description": "Thrilling adventure experience",
"price": 299.99,
"imageUrl": "http://example.com/package.jpg"
}


PUT http://localhost:8080/providers/{providerId}/packages/{packageId} - Update an existing package.
Path Variables: {providerId} - ID of the provider, {packageId} - ID of the package to update.
Body:
json

{
"name": "Updated Adventure Package",
"description": "Updated thrilling experience",
"price": 349.99,
"imageUrl": "http://example.com/updatedpackage.jpg"
}


DELETE http://localhost:8080/providers/{providerId}/packages/{packageId} - Delete a package.
Path Variables: {providerId} - ID of the provider, {packageId} - ID of the package to delete.
GET http://localhost:8080/providers/{providerId}/packages - Retrieve all packages for a provider.
Path Variable: {providerId} - Use the ID of the provider to get their packages.

Booking Management Endpoints
GET http://localhost:8080/providers/{providerId}/bookings - Retrieve all bookings associated with a provider’s packages.
Path Variable: {providerId} - Use the ID of the provider.

Customer Statistics for Provider
GET http://localhost:8080/providers/{providerId}/statistics - Retrieve statistics (likes, dislikes, bookings) for each package of the provider.
Path Variable: {providerId} - Use the ID of the provider.

Review Management Endpoints
GET http://localhost:8080/providers/{providerId}/reviews - Retrieve all reviews for a provider’s packages.
Path Variable: {providerId} - Use the ID of the provider.

Report Management Endpoint
POST http://localhost:8080/providers/newReport/{reviewId}/users/{reporterId}
Description: Create a new report on a review.
Path Variables:
{reviewId} - ID of the review being reported
{reporterId} - ID of the user reporting the review
Body (JSON):
{
  "reason": "Inappropriate content",
  "status": "PENDING"
}



