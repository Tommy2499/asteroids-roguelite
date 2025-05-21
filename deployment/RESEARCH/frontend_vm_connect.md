# Research Report
## Hosting Spring Boot Backend with Frontend on School VM
### Summary of Work
<!--One paragraph summary of the research being performed-->

I tried to host a React frontend on `https://pages.cs.wisc.edu/~taejeong/` and connect it to a backend written in Java Spring Boot, which runs along with a MySQL database inside Docker on my school VM. The frontend was successfully deployed using the `public/html` directory, but connecting to the backend failed due to mixed content errors and network restrictions imposed by the CSL. This led me to explore alternative ways to make the entire application run on the school VM itself.

### Motivation
<!--Explain why you felt the need to perform this research-->

My CS506 project requires a full-stack web application with a persistent database to store user and leaderboard data. While the CSL offers a way to host personal websites via `pages.cs.wisc.edu`, it does not support backend scripts or services. I was looking for a way to have a public frontend interface while keeping the backend within my school VM, fulfilling both deployment and Docker requirements.

### Time Spent
<!--Explain how your time was spent-->
- 30 minutes setting up pages.cs.wisc.edu and uploading frontend
- 45 minutes configuring Spring Boot backend and MySQL in Docker
- 45 minutes debugging mixed content (HTTPS/HTTP) errors
- 30 minutes researching CSL firewall/network rules
- 30 minutes testing SSH port forwarding and localhost tunneling

### Results
<!--Explain what you learned/produced/etc. This section should explain the
important things you learned so that it can serve as an easy reference for yourself
and others who could benefit from reviewing this topic. Include your sources as
footnotes. Make sure you include the footnotes where appropriate e.g [^1]-->

I found that while the frontend can be hosted publicly using CSL’s `pages.cs.wisc.edu`, it is served over HTTPS, and cannot connect to a backend that is only available over HTTP (which is the case with my Dockerized backend on the VM). Browsers block these types of requests for security reasons (mixed content error) [^1].

Additionally, the CSL firewall restricts incoming traffic to most ports, especially those commonly used by web servers like 8080 [^2]. Because of this, exposing the backend to the internet from the school VM is not possible unless I get help from CSL directly.

As a result, I concluded that the only stable solution was to run both frontend and backend inside the same VM, possibly served through a reverse proxy or hosted as a single-page app directly from the backend.

I also tested SSH port forwarding using:
ssh -L 5173:localhost:5173 -L 8080:localhost:8080 taejeong@cs506x07.cs.wisc.edu

Which worked locally, but does not make the backend public to other users.

### Sources
<!--list your sources and link them to a footnote with the source url-->
- CSL Web Hosting Info[^1]
- CSL Network Restrictions[^2]
- Mixed Content Docs[^3]
- SSH Tunnel Docs[^4]

[^1]: https://csl.cs.wisc.edu/webhosting/
[^2]: https://csl.cs.wisc.edu/networking/#insecure
[^3]: https://developer.mozilla.org/en-US/docs/Web/Security/Mixed_content
[^4]: https://www.ssh.com/academy/ssh/tunneling
