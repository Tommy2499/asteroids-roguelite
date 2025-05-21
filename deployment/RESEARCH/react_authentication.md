# Research Report
## React Authentication
### Summary of Work
<!--One paragraph summary of the research being performed-->
This research focused on implementing authentication in a React app, specifically how to restrict certain routes so that they can only be accessed by logged-in users.
The solution uses React Context to manage user authentication state and a custom ProtectedRoute component to handle route protection using React Router.
The purpose of this was to support user login logic and maintain a clean, scalable project structure.

### Motivation
<!--Explain why you felt the need to perform this research-->
As part of our React-based project, we needed a way to prevent users from accessing pages unless they were authenticated.
Many of our pages are tied to a specific user, so allowing a guest to access these pages would cause many issues.
I wasn’t familiar with best practices for managing authentication in React, so I wanted to explore options that were both clean and flexible enough for future features like role-based access or persistent login.

### Time Spent
<!--Explain how your time was spent-->
Roughly 5 hours were spent on this task.
About 1 hour was used reviewing documentation and tutorials.
2 hours writing and testing the AuthContext and ProtectedRoute components.
2 more hours were spent debugging and making sure the components integrated well with our router setup and login flow.
The last 1 hour was administrative (this report, merge request, comments and documentation).

### Results
<!--Explain what you learned/produced/etc. This section should explain the
important things you learned so that it can serve as an easy reference for yourself
and others who could benefit from reviewing this topic. Include your sources as
footnotes. Make sure you include the footnotes where appropriate e.g [^1]-->

I learned how to implement authentication in a React app using context and protected routes. Here are the key takeaways:

- React Context is ideal for managing global authentication state (user, login, logout) in a clean and reusable way.
- useContext allows any component to access the auth state via a custom useAuth() hook.
- ProtectedRoute wraps route components and redirects users to the login page if they aren't authenticated.
- React Router simplifies route protection using conditional rendering and the Navigate component.

This pattern is very extendable.
Future additions, such as storing tokens in localStorage or adding roles (like admin access), could be built from this setup.

Key Components:

1. AuthContext.js: Manages login state, provides login() and logout() functions, and wraps the app to make these available anywhere.
2. ProtectedRoute.js: Redirects users to the login page if they are not authenticated. Otherwise, renders the child component.
3. Usage with React Router: Use `<ProtectedRoute>` to wrap any route component you want to secure.

This approach keeps logic centralized, avoids prop drilling, and makes it easy to lock down parts of your app without bloating components with auth logic.

### Sources
<!--list your sources and link them to a footnote with the source url-->
- React Context[^1]
- React Router ProtectedRoutes and Authentication[^2]
[^1]: https://react.dev/learn/passing-data-deeply-with-context
[^2]: https://ui.dev/react-router-protected-routes-authentication