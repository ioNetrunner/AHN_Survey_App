**Survey Options**
----
  Returns json array of survey meta data.

* **URL**

  /api/surveys

* **Method:**

  `GET`
  
* **URL Params**

  None

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `[{ ID : 1, name : "COPD Assessment Test", lang : "en-US", version : 1 }, { etc } ]`
 
* **Error Response:**

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ error : "You are unauthorized to make this request." }`

* **Sample Call:**

  ```javascript
    $.ajax({
      url: "/api/surveys/",
      dataType: "json",
      type : "GET",
      success : function(r) {
        console.log(r);
      }
    });
  ```

**Show Survey**
----
  Returns json array of survey questions

* **URL**

  /api/surveys/:ID

* **Method:**

  `GET`
  
*  **URL Params**

   **Required:**
 
   `ID=[integer]`

* **Data Params**

  None

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `[{ low_text : "example", high_text : "example", minimum : 0, maximum : 5, interval : 1 }, { low_text : "example", high_text : "example", minimum : 0, maximum : 5, interval : 1 }, { etc } ]`
 
* **Error Response:**

  * **Code:** 404 NOT FOUND <br />
    **Content:** `{ error : "Survey doesn't exist" }`

  OR

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ error : "You are unauthorized to make this request." }`

* **Sample Call:**

  ```javascript
    $.ajax({
      url: "/api/surveys/1",
      dataType: "json",
      type : "GET",
      success : function(r) {
        console.log(r);
      }
    });
  ```
