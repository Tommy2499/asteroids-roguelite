# Research Report  
## Hosting Full Stack App on School VM with WiscVPN  
### Summary of Work  
<!--One paragraph summary of the research being performed-->

I researched how to host a full-stack application—including a React frontend, 
a Spring Boot backend, and a MySQL database—entirely inside Docker on my school VM.
To make it accessible, I configured Docker to expose ports allowed through the CSL firewall 
and verified that users can connect through WiscVPN. This setup keeps the application 
in a single environment and satisfies the requirement for Docker-based deployment 
while still allowing external access (via VPN).

### Motivation  
<!--Explain why you felt the need to perform this research-->

After I failed to connect my HTTPS frontend on `pages.cs.wisc.edu` to the HTTP backend running
 in Docker due to mixed content errors and firewall restrictions, I needed an alternative.
The application must persist data with MySQL and support public-ish access for the team and TA.
Running everything on the school VM using ports open to VPN users gave me a working,
contained solution without relying on external hosting services.

### Time Spent  
<!--Explain how your time was spent-->
- 30 minutes modifying Docker Compose to expose public ports (80, 3000, 3306, 5000)  
- 25 minutes updating Vite and Spring Boot configs  
- 20 minutes verifying VPN access with different networks  
- 20 minutes enabling persistent Docker with `loginctl enable-linger`  
- 15 minutes testing full app functionality via IP and ports  

### Results  
<!--Explain what you learned/produced/etc. This section should explain the
important things you learned so that it can serve as an easy reference for yourself
and others who could benefit from reviewing this topic. Include your sources as
footnotes. Make sure you include the footnotes where appropriate e.g [^1]-->

I successfully hosted all three components (frontend, backend, database) in Docker using 
`docker-compose`, and mapped them to firewall-allowed ports.
I confirmed that the app can be accessed using URLs like:

- `http://cs506x07.cs.wisc.edu:3000` for the frontend  
- `http://cs506x07.cs.wisc.edu:5000` for the backend  
- `http://cs506x07.cs.wisc.edu:80` for phpMyAdmin  

These are accessible only for users connected to WiscVPN. I enabled persistent background Docker processes
using `loginctl enable-linger` so the containers keep running even after logout. 
This approach avoids HTTPS-related issues and lets me fulfill all project requirements within the school’s infrastructure.

### Sources  
<!--list your sources and link them to a footnote with the source url-->
- CSL Web Hosting Info[^1]  
- CSL Docker Guide[^2]  
- CSL Contacted via Email[^3]

[^1]: https://it.wisc.edu/services/web-hosting/  
[^2]: https://csl.cs.wisc.edu/docs/csl/docker/#requesting-docker  
[^3]: lab@cs.wisc.edu (Used Wisc email to ask about public port access)
