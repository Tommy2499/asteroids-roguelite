/** IMPORTANT
 * As of current implementation, you need to run the following commands in the my-app directory:
 * npm install
 * npm run dev
 */

import React, {useState} from 'react'
import './home.css'
import logo from './images/asteroid-logo-bgless.png'
import { Link, useNavigate } from 'react-router-dom'; // Import useNavigate for navigation
import { useAuth } from './auth_context'; // Import useAuth for authentication context

/**
 * This function designs the frontend of the home/login page.
 * It returns HTML content to be rendered to the page.
 * The page allows the user to login and upon success, directs 
 * the user to the game.
 * 
 * @returns HTML - Page design to be rendered
 */
function Home() {

    /**
     * username input by the user
     * 
     * see src/main/java/com/pluto/app/LocalController.java for format specifications
     */
    const [username, setUsername] = useState('');
    /**
     * password input by the user
     * 
     * may include special characters which must be encoded in order for them to
     * not be treated as such within the URI of the HTTP request
     * 
     * see src/main/java/com/pluto/app/LocalController.java for format specifications
     */
    const [password, setPassword] = useState('');
    const [timeTaken, setTimeTaken] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate(); // Initialize useNavigate
    const auth = useAuth(); // Access authentication context

    /**
     * This method sends user login requests to the backend at localhost:8080/api/login
     * 
     * @param username - The username entered by the user
     * @param password - The password entered by the user
     * @returns {Promise<void>}
     * 
     */
    const handleLogin = async () => {
        const startTime = performance.now(); // Start timer

        const response = await fetch(`http://137.184.232.147:5000/api/login?name=${encodeURIComponent(username)}&pass=${encodeURIComponent(password)}`);
        const data = await response.json();

        const endTime = performance.now(); // End timer
        setTimeTaken((endTime - startTime).toFixed(2)); // Calculate time in ms

        // Log success and error messages
        console.log(`Log in clicked. Username: ${encodeURIComponent(username)} Password: ${encodeURIComponent(password)}`);
        console.log(`Success: ${data.success}, Error: ${data.error}`);

        if (data.success === 'true') {
            auth.login({ username }); // Log in the user
            navigate('/main_menu'); // Redirect to /main_menu on success
        } else {
            setErrorMessage(data.error); // Display error message on failure
        }
    }

    /**
     * This method sends user registration requests to the backend at localhost:8080/api/register
     * 
     * @param username - The username entered by the user
     * @param password - The password entered by the user
     * @returns {Promise<void>}
     * 
     */
    const handleRegister = async () => {
        const startTime = performance.now(); // Start timer

        const response = await fetch(`http://137.184.232.147:5000/api/register?name=${encodeURIComponent(username)}&pass=${encodeURIComponent(password)}`);
        const data = await response.json();

        const profileResponse = await fetch(`http://137.184.232.147:5000/api/createProfile?username=${encodeURIComponent(username)}&profile_name=${encodeURIComponent(username)}`)
        const profileData = await profileResponse.json();

        const endTime = performance.now(); // End timer
        setTimeTaken((endTime - startTime).toFixed(2)); // Calculate time in ms

        // Log success and error messages
        console.log(`Register clicked. Username: ${encodeURIComponent(username)} Password: ${encodeURIComponent(password)}`);
        console.log(`Success: ${data.success}, Error: ${data.error}`);

        console.log(`Create profile initiated. Profile name: ${encodeURIComponent(username)}`)
        console.log(`Success: ${profileData.success}, Error: ${profileData.error}`)

        setErrorMessage(prev => {
            const message = data.error === "" ? "Registration successful!" : data.error; // Registration success message
            console.log("Updated Error:", message);
            return message;
        });
    }

    return (

        // Overarching page container
        <div className='home-container'>

            {/** Main content of the page */}
            <div className='home-content'>

                {/** Font moves the letters to the left,
                 * so custom padding combats this */}
                <div className='title-container'>
                    <div className='title-padding'/>
                    <div className='title'>Asteroids</div>
                </div>

                <img src={logo} className="login-logo" alt="Asteroids logo" />

                <div className='login-pass-container'>

                    <div className='input-container'>
                        {/** Allows user to input text.
                         * This text is saved in value, then stored in global username. */}
                        <label>Username:</label>
                        <input type='text' value={username} onChange={(e) => setUsername(e.target.value)}/>
                    </div>

                    <div className='input-container'>
                        {/** Allows user to input a password.
                         * Type password just hides the user's typed input
                         * unless the user clicks the eye icon that is
                         * automatically placed at the end of the input box. */}
                        <label>Password:</label>
                        <input type='password' value={password} onChange={(e) => setPassword(e.target.value)}/>
                    </div>

                </div>

                {/** Buttons to log in or register.
                 * When clicked, handleLogin or handleRegister is executed. */}
                <div className='login-register-container'>
                    <button className='login-register' onClick={handleLogin}>Log In</button>
                    <button className='login-register' onClick={handleRegister}>Register</button>
                </div>
                
                <div>
                    {/** Displays error message if login or registration fails */}
                    <p className='error-message'>{errorMessage}</p>
                </div>

            </div>

        </div>
    );
}

export default Home;
