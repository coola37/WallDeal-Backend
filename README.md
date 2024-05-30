# Rest API for WallDeal
## Prepared for WallDeal -> `https://github.com/coola37/WallDeal`

## Tools and libraries: 
<p>Spring Boot, Spring Security, Spring Cloud</p>
<p>Redis</p>
<p>Firebase Authentication, Firestore Database, Firebase Messaging, Cloud Functions</p>
<p>PostgreSQL</p>
<p>JPA Repository</p>
<p>AWS - Elastic Container Services</p>
<p>API Gateway</p>
<p>Docker</p>


# API Documentation

## UserController

### Endpoints:

**Save User**
   - **Method:** POST
   - **URL:** `/api/1.0/users`
   - **Description:** Creates a new user.
   - **Request Body:** User object in JSON format.
   - **Response:** User object with saved details.

**Get User by ID**
   - **Method:** GET
   - **URL:** `/api/1.0/users/{userId}`
   - **Description:** Retrieves user details by ID.
   - **Request Headers:** Authorization: Firebase token
   - **Response:** User object with requested ID.

**Get All Users**
   - **Method:** GET
   - **URL:** `/api/1.0/users`
   - **Description:** Retrieves all users.
   - **Request Headers:** Authorization: Firebase token
   - **Response:** Set of user objects.

**Check Favorites**
   - **Method:** GET
   - **URL:** `/api/1.0/users/check/favorites/{userId}/{wallpaperId}`
   - **Description:** Checks if a wallpaper is in a user's favorites.
   - **Response:** Boolean value indicating whether the wallpaper is in favorites.

**Follow or Unfollow User**
   - **Method:** PUT
   - **URL:** `/api/1.0/users/follow-or-unfollow/{currentUserId}/{targetUserId}`
   - **Description:** Allows a user to follow or unfollow another user.
   - **Response:** Success status.

**Check Follow**
   - **Method:** GET
   - **URL:** `/api/1.0/users/check-follow/{currentUserId}/{targetUserId}`
   - **Description:** Checks if a user is following another user.
   - **Response:** Boolean value indicating the follow status.

**Edit Profile**
   - **Method:** PUT
   - **URL:** `/api/1.0/users/edit-profile`
   - **Description:** Edits user profile details.
   - **Request Body:** User object with updated details.
   - **Response:** Success status.

**Delete User**
   - **Method:** DELETE
   - **URL:** `/api/1.0/users/delete-user/{userId}`
   - **Description:** Deletes a user account.
   - **Response:** Success status.

**Check User**
   - **Method:** GET
   - **URL:** `/api/1.0/users/check-user/{userId}`
   - **Description:** Checks if a user exists.
   - **Response:** Boolean value indicating user existence.

**Username Check**
    - **Method:** GET
    - **URL:** `/api/1.0/users/username-check/{username}`
    - **Description:** Checks if a username is already in use.
    - **Response:** Boolean value indicating username availability.

## WallpaperController

### Endpoints:

**Save Wallpaper**
   - **Method:** POST
   - **URL:** `/api/1.0/wallpapers`
   - **Description:** Saves a new wallpaper.
   - **Request Headers:** Authorization: Firebase token
   - **Request Body:** Wallpaper object in JSON format.
   - **Response:** Success status.

**Get Wallpapers**
   - **Method:** GET
   - **URL:** `/api/1.0/wallpapers`
   - **Description:** Retrieves all wallpapers.
   - **Response:** List of wallpaper objects.

**Get Wallpaper by Category**
   - **Method:** GET
   - **URL:** `/api/1.0/wallpapers/category/{category}`
   - **Description:** Retrieves wallpapers by category.
   - **Response:** List of wallpaper objects filtered by category.

**Get Wallpaper by ID**
   - **Method:** GET
   - **URL:** `/api/1.0/wallpapers/get/{wallpaperId}`
   - **Description:** Retrieves wallpaper details by ID.
   - **Response:** Wallpaper object with requested ID.

**Get Wallpaper by Owner**
   - **Method:** GET
   - **URL:** `/api/1.0/wallpapers/owner/{id}`
   - **Description:** Retrieves wallpapers by owner ID.
   - **Response:** List of wallpaper objects owned by the specified user.

**Like or Dislike Wallpaper**
   - **Method:** PUT
   - **URL:** `/api/1.0/wallpapers/like/{wallpaperId}`
   - **Description:** Likes or unlikes a wallpaper.
   - **Request Body:** LikeRequest object in JSON format.
   - **Response:** Success status.

**Check Like**
   - **Method:** GET
   - **URL:** `/api/1.0/wallpapers/like/control/{wallpaperId}/{currentUserId}`
   - **Description:** Checks if a user has liked a wallpaper.
   - **Response:** Boolean value indicating like status.

**Add Favorite**
   - **Method:** PUT
   - **URL:** `/api/1.0/wallpapers/favorite/{userId}/{wallpaperId}`
   - **Description:** Adds a wallpaper to user's favorites.
   - **Response:** Success status.

**Get Wallpapers by Followed Users**
   - **Method:** GET
   - **URL:** `/api/1.0/wallpapers/get-wallpapers-by-followed/{currentUserId}`
   - **Description:** Retrieves wallpapers uploaded by followed users.
   - **Response:** List of wallpaper objects.

**Remove Wallpaper**
    - **Method:** DELETE
    - **URL:** `/api/1.0/wallpapers/delete/{wallpaperId}`
    - **Description:** Removes a wallpaper.
    - **Response:** Success status.

**Get Favorite Wallpapers**
    - **Method:** GET
    - **URL:** `/api/1.0/wallpapers/get-favorite-wallpaper/{userId}`
    - **Description:** Retrieves favorite wallpapers of a user.
    - **Response:** List of wallpaper objects marked as favorites by the user.

## CoupleController

### Endpoints:

# Send Request

- **Method:** POST
- **URL:** `/api/1.0/walldeal/send-request/{senderUser}/{receiverUser}`
- **Description:** Sends a couple request from the sender user to the receiver user.
- **Parameters:**
  - `senderUser` (String): The user sending the request.
  - `receiverUser` (String): The user receiving the request.
- **Response:** No content.

# Delete Request

- **Method:** DELETE
- **URL:** `/api/1.0/walldeal/delete-request/{requestId}`
- **Description:** Deletes a couple request.
- **Parameters:**
  - `requestId` (String): The ID of the request to be deleted.
- **Response:** Success status.

# Check WallDeal Request

- **Method:** GET
- **URL:** `/api/1.0/walldeal/check-walldeal-request/{currentUserId}/{targetUserId}`
- **Description:** Checks if a couple request exists between two users.
- **Parameters:**
  - `currentUserId` (String): The current user.
  - `targetUserId` (String): The target user.
- **Response:** Boolean value indicating request existence.

# Check WallDeal

- **Method:** GET
- **URL:** `/api/1.0/walldeal/check-walldeal/{targetUserId}`
- **Description:** Checks if a couple exists for the specified user.
- **Parameters:**
  - `targetUserId` (String): The user whose couple status is to be checked.
- **Response:** Boolean value indicating couple existence.

# Check WallDeal For Between User To User

- **Method:** GET
- **URL:** `/api/1.0/walldeal/check-walldeal-for-between-user-to-user/{currentUserId}/{targetUserId}`
- **Description:** Checks if a couple exists between two specified users.
- **Parameters:**
  - `currentUserId` (String): The current user.
  - `targetUserId` (String): The target user.
- **Response:** Boolean value indicating couple existence.

# Send Post

- **Method:** PUT
- **URL:** `/api/1.0/walldeal/send-post/{currentUserId}`
- **Description:** Sends a wallpaper request for the current user.
- **Parameters:**
  - `currentUserId` (String): The current user.
  - `request` (WallpaperRequest): The wallpaper request.
- **Response:** No content.

# Cancel Post

- **Method:** PUT
- **URL:** `/api/1.0/walldeal/cancel-post/{currentUserId}`
- **Description:** Cancels a wallpaper request for the current user.
- **Parameters:**
  - `currentUserId` (String): The current user.
  - `request` (WallpaperRequest): The wallpaper request.
- **Response:** No content.

# Get Requests By UserId

- **Method:** GET
- **URL:** `/api/1.0/walldeal/get-walldeal-request/{userId}`
- **Description:** Retrieves requests by user ID.
- **Parameters:**
  - `userId` (String): The user ID.
- **Response:** A list containing the user's requests.

# Get Wallpaper Request

- **Method:** GET
- **URL:** `/api/1.0/walldeal/get-wallpaper-request/{requestId}`
- **Description:** Retrieves a wallpaper request by its ID.
- **Parameters:**
  - `requestId` (String): The ID of the request.
- **Response:** The requested wallpaper request.

# Check Request Notifications

- **Method:** GET
- **URL:** `/api/1.0/walldeal/request-notification-check/{userId}`
- **Description:** Checks request notifications.
- **Parameters:**
  - `userId` (String): The user ID.
- **Response:** Boolean value indicating the presence of request notifications.

# Create WallDeal

- **Method:** POST
- **URL:** `/api/1.0/walldeal/create-walldeal`
- **Description:** Creates a couple.
- **Parameters:**
  - `couple` (Couple): The couple details.
- **Response:** Success status.

# Get WallDeal

- **Method:** GET
- **URL:** `/api/1.0/walldeal/get-walldeal/{userId}`
- **Description:** Retrieves the current couple for the specified user ID.
- **Parameters:**
  - `userId` (String): The user ID.
- **Response:** The current couple details.

# Cancel WallDeal

- **Method:** DELETE
- **URL:** `/api/1.0/walldeal/cancel-walldeal/{userId}`
- **Description:** Cancels the current couple for the specified user ID.
- **Parameters:**
  - `userId` (String): The user ID.
- **Response:** Success status.

