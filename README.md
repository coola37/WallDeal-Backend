# Rest API for WallDeal

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

**Send Request**

- **Method:** POST
- **URL:** `/api/1.0/walldeal/send-request/{senderUser}/{receiverUser}`
- **Description:** Gönderen kullanıcıdan alıcı kullanıcıya çift isteği gönderir.
- **Parameters:**
  - `senderUser` (String): İsteği gönderen kullanıcı.
  - `receiverUser` (String): İsteği alan kullanıcı.
- **Response:** No content.

**Delete Request**

- **Method:** DELETE
- **URL:** `/api/1.0/walldeal/delete-request/{requestId}`
- **Description:** Bir çift isteğini siler.
- **Parameters:**
  - `requestId` (String): Silinecek isteğin kimliği.
- **Response:** Başarılı olursa HTTP durumu.

**Check WallDeal Request**

- **Method:** GET
- **URL:** `/api/1.0/walldeal/check-walldeal-request/{currentUserId}/{targetUserId}`
- **Description:** İki kullanıcı arasında çift isteği olup olmadığını kontrol eder.
- **Parameters:**
  - `currentUserId` (String): Mevcut kullanıcı.
  - `targetUserId` (String): Hedef kullanıcı.
- **Response:** İsteğin var olup olmadığını belirten Boolean değeri.

**Check WallDeal**

- **Method:** GET
- **URL:** `/api/1.0/walldeal/check-walldeal/{targetUserId}`
- **Description:** Belirtilen kullanıcı için bir çift olup olmadığını kontrol eder.
- **Parameters:**
  - `targetUserId` (String): Mevcut çifti kontrol edilecek kullanıcı.
- **Response:** Çiftin var olup olmadığını belirten Boolean değeri.

**Check WallDeal For Between User To User**

- **Method:** GET
- **URL:** `/api/1.0/walldeal/check-walldeal-for-between-user-to-user/{currentUserId}/{targetUserId}`
- **Description:** İki belirli kullanıcı arasında çift olup olmadığını kontrol eder.
- **Parameters:**
  - `currentUserId` (String): Mevcut kullanıcı.
  - `targetUserId` (String): Hedef kullanıcı.
- **Response:** Çiftin var olup olmadığını belirten Boolean değeri.

**Send Post**

- **Method:** PUT
- **URL:** `/api/1.0/walldeal/send-post/{currentUserId}`
- **Description:** Mevcut kullanıcı için bir duvar kağıdı talebi gönderir.
- **Parameters:**
  - `currentUserId` (String): Mevcut kullanıcı.
  - `request` (WallpaperRequest): Duvar kağıdı talebi.
- **Response:** No content.

**Cancel Post**

- **Method:** PUT
- **URL:** `/api/1.0/walldeal/cancel-post/{currentUserId}`
- **Description:** Mevcut kullanıcı için bir duvar kağıdı talebini iptal eder.
- **Parameters:**
  - `currentUserId` (String): Mevcut kullanıcı.
  - `request` (WallpaperRequest): Duvar kağıdı talebi.
- **Response:** No content.

**Get Requests By UserId**

- **Method:** GET
- **URL:** `/api/1.0/walldeal/get-walldeal-request/{userId}`
- **Description:** Kullanıcı kimliğine göre talepleri getirir.
- **Parameters:**
  - `userId` (String): Kullanıcı kimliği.
- **Response:** Kullanıcının taleplerini içeren bir liste.

**Get Wallpaper Request**

- **Method:** GET
- **URL:** `/api/1.0/walldeal/get-wallpaper-request/{requestId}`
- **Description:** Bir duvar kağıdı talebini getirir.
- **Parameters:**
  - `requestId` (String): Talebin kimliği.
- **Response:** İstenen duvar kağıdı talebi.

**Check Request Notifications**

- **Method:** GET
- **URL:** `/api/1.0/walldeal/request-notification-check/{userId}`
- **Description:** İstek bildirimlerini kontrol eder.
- **Parameters:**
  - `userId` (String): Kullanıcı kimliği.
- **Response:** İstek bildirimlerinin olup olmadığını belirten Boolean değeri.

**Create WallDeal**

- **Method:** POST
- **URL:** `/api/1.0/walldeal/create-walldeal`
- **Description:** Bir çift oluşturur.
- **Parameters:**
  - `couple` (Couple): Çift bilgileri.
- **Response:** Başarılı olursa HTTP durumu.

**Get WallDeal**

- **Method:** GET
- **URL:** `/api/1.0/walldeal/get-walldeal/{userId}`
- **Description:** Kullanıcı kimliğine göre mevcut çifti getirir.
- **Parameters:**
  - `userId` (String): Kullanıcı kimliği.
- **Response:** Mevcut çift bilgileri.

**Cancel WallDeal**

- **Method:** DELETE
- **URL:** `/api/1.0/walldeal/cancel-walldeal/{userId}`
- **Description:** Kullanıcı kimliğine göre mevcut çifti iptal eder.
- **Parameters:**
  - `userId` (String): Kullanıcı kimliği.
- **Response:** Başarılı olursa HTTP durumu.
