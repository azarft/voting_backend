# Technical Decisions & Security Implementation

## User Identification and Integrity

One of the core requirements for a fair voting system is the "one person - one vote" principle. To achieve this without complex KYC (Know Your Customer) processes, we have implemented the following strategy:

### 1. Mandatory University Domain Emails
The system only allows registration and login using the official university domain: **`@alatoo.edu.kg`**.

### 2. Why this decision was made:
- **Identity Verification**: Every student and staff member at Alatoo International University is issued exactly one unique email address upon enrollment/employment.
- **Sybil Attack Prevention**: By restricting the domain, we prevent malicious actors from creating multiple accounts using public providers (Gmail, Outlook, etc.) to manipulate voting results.
- **No Manual Registration**: The system treats the email as the primary key of identity. Since only the university administration can issue these emails, we delegate the "identity proof" to the university's IT infrastructure.
- **Traceability**: In case of any technical issues or audits, the university email provides a clear link to the institutional identity.

### 3. Verification Process
1. User enters their university email.
2. The `auth-service` verifies the domain suffix.
3. A one-time verification code is sent to that email.
4. Only the owner of the university account can access the code, ensuring that the person voting is indeed the authorized student/staff member.

### 4. Duplicate Vote Prevention
Even with unique accounts, the system enforces integrity at the database level:
- The `votes` table in the `result_schema` has a unique constraint on `(user_id, session_id)`.
- The `result-service` consumer checks for duplicates before persistence.
- This ensures that even if a user tries to send multiple requests, only the first one is recorded.
