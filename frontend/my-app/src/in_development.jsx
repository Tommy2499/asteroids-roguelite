import React from 'react';
import './in_development.css';
import ship from './images/asteroid-logo-bgless.png';
import { Link } from 'react-router-dom';

function InDevelopment(props) {
    return (
        // Main container for the "In Development" page
        <div id='in-dev-container'>
            <div id='in-dev-content'>
                {/* Back button to return to the main menu */}
                <Link to='/main_menu' id='dev-back-link'>
                    <button className='button-2' id='dev-back-button'>{'< '}Back</button>
                </Link>
                {/* Text content for the "In Development" message */}
                <div id='dev-text'>
                    <div id='dev-intro'>
                        {/* Display the page name dynamically */}
                        The {props.page} page is currently under construction. Enjoy the rest of the Asteroids app and check back in a few days!
                    </div>
                    <div id='dev-description'>
                        {/* Additional description passed as a prop */}
                        {props.description}
                    </div>
                </div>
                {/* Orbiting ship animation */}
                <div className='orbit-container'>
                    <div className='orbit-wrapper'>
                        <img src={ship} id='dev-ship' alt='Orbiting ship'/>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default InDevelopment;