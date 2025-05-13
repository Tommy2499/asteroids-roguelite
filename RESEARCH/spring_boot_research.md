# Research Report
## Spring Boot and React Stack
### Summary of Work
<!--One paragraph summary of the research being performed-->

I researched how to use Java Spring Boot and how to send responses to a React frontend through REST. I spent time learning how to set up the enviornment in order to use both Spring Boot and React in one project through the maven, npm, and spring initializer. I had to research a bit more on how to do REST calls and http calls and requests. After, I was able to create a simple demo that uses this tech stack in order to create a demo website that takes in user names and outputs a string including their name. The main focus of this research is Spring Boot. 


### Motivation
<!--Explain why you felt the need to perform this research-->

We had a gRPC demo that worked but also wanted to explore other options like REST from the readings, so I created a demo for this. I'm a little familiar with gRPC but was also curious if there was a more developer-friendly method (which we found JSON and REST was). While there are concerns for latency, we decided that React/Spring Boot with JSONs will be our tech stack. 

### Time Spent
<!--Explain how your time was spent-->
- 15 minutes reading a springboot-react guide
- 10 minutes a CRUD guide
- 5 minutes looking for pacakge manager + frameworks
- 30 minutes developing
- 10 minutes asking ChatGPT for debugging help

### Results
<!--Explain what you learned/produced/etc. This section should explain the
important things you learned so that it can serve as an easy reference for yourself
and others who could benefit from reviewing this topic. Include your sources as
footnotes. Make sure you include the footnotes where appropriate e.g [^1]-->

I was able to create a framework using spring.io to create a maven-javen with the spring web dependency that includes RESTful and Tomcat to host the backend [^1]. I was also able to make the React frontend with npm and vite. 

I was able to create a demo [^2] that sends a string in JSON format from the React frontend to the Spring Boot backend using http calls. The frontend took in a string input in an async function:

`App.jsx`
```
const fetchGreeting = async () => {
    const startTime = performance.now(); // Start timer

    const response = await fetch(`http://localhost:8080/api/greet?name=${name}`);
    const data = await response.json();

    const endTime = performance.now(); // End timer
    setTimeTaken((endTime - startTime).toFixed(2)); // Calculate time in ms

    setGreeting(data.message);
  };
```

I was able to make these calls to my Tomcat server using this controller:

`HelloController.java`
```
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // Allow frontend to call backend
public class HelloController {
    
    @GetMapping("/greet")
    public String greet(@RequestParam(defaultValue = "World") String name) {
        return "{\"message\": \"Hello, " + name + "!\"}";
    }
}
```

This sends an http request from the running frontend on default port 5173 to the running backend server on default port 8080, and responds with a string back. 

This code is a combination these two guides [^3] [^4] and looking up frameworks and package managers for this demo [^5] [^6] [^1]

A lot of the documentation is in this spring guide [^7] and includes much more detail than I can put here. 

### Sources
<!--list your sources and link them to a footnote with the source url-->
- spring.io[^1]
- demo[^2]
- guide1[^3]
- guide2 with spring.io[^4]
- maven[^5]
- npm[^6]
- spring guide[^7]

[^1]: spring.io(https://start.spring.io/)
[^2]: demo(https://git.doit.wisc.edu/cdis/cs/courses/cs506/sp2025/team/T_07/research-springboot)
[^3]: guide1(https://www.dhiwise.com/post/a-step-by-step-guide-to-implementing-react-spring-boot)
[^4]: guide2(https://developer.okta.com/blog/2022/06/17/simple-crud-react-and-spring-boot)
[^5]: maven(https://maven.apache.org/download.cgi)
[^6]: npm(https://nodejs.org/en/download/)
[^7]: spring_guide(https://docs.spring.io/spring-boot/tutorial/first-application/index.html)

