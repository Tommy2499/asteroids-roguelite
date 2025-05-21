// Importing required CSS and assets

// Highest specificity stylesheet
import './main_menu.css';
// Backgroundless png of logo
import logo from './images/asteroid-logo-bgless.png';
import React, {useState} from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from './auth_context';

// Pascal case for React Components
import StatIcon from './images/stat-icon.svg?react';
import LeaderboardIcon from './images/leaderboard-icon.svg?react';
import CosmeticsIcon from './images/cosmetics-icon.svg?react';
import SettingsIcon from './images/settings-icon.svg?react';
import ExitIcon from './images/exit-icon.svg?react';

// Snake case for variable
import profile_icon from './images/profile-icon.png';

/**
 * MainMenu component renders the main menu of the Asteroids game after logging in.
 * It includes the game title, logo, play button, and navigation icons
 * for Stats, Leaderboard, Cosmetics, and Settings. It also includes
 * links to the Profiles page and an Exit button.
 *
 * @returns {JSX.Element} The JSX structure for the main menu.
 */
function MainMenu() {

    const auth = useAuth();
    const navigate = useNavigate();

    const handleExit = async () => {
        auth.logout();
        navigate('/');
    }

    return (
        // Main container for the menu
        <div className='main-menu-container'>

            {/* Left side of the menu, containing the Profiles link */}
            <div className='side'>

                <div className='exit-set'>
                    {/* Link to the Profiles page */}
                    <Link to='/profiles' className='icon-link'>
                        <div className='circular-container'>
                            <img src={profile_icon} className='profile-icon' alt='Profile Icon' />
                        </div>
                    </Link>
                    <div className='exit-label'>Profiles</div>
                </div>

            </div>

            {/* Center content of the menu */}
            <div className='main-menu-content'>

                {/* Title section */}
                <div className='title-container'>
                    <div className='title-padding'/> {/* Padding to adjust for font offset */}
                    <div className='title'>Asteroids</div>
                </div>

                {/* Logo image */}
                <img src={logo} className='main-menu-logo' alt='Asteroids logo' />

                {/* Play button */}
                <Link to='/play'>
                    <button className='play-button'>Play</button> {/* Link to the gameplay page */}
                </Link>

                {/* Icon section for additional menu options */}
                <div className='icon-container'>

                    {/* Individual icon sets */}
                    <div className='icon-set'>
                        {/* Link to the Stats page */}
                        <Link to='/stats'>
                            <StatIcon className='menu-icon' />
                        </Link>
                        <div className='icon-label'>Stats</div>
                    </div>

                    <div className='icon-set'>
                        {/* Link to the Leaderboard page */}
                        <Link to='/leaderboard'>
                            <LeaderboardIcon className='menu-icon' />
                        </Link>
                        <div className='icon-label'>Leaderboard</div>
                    </div>

                    <div className='icon-set'>
                        {/* Link to the Cosmetics page */}
                        <Link to='/cosmetics'>
                            <CosmeticsIcon className='menu-icon' />
                        </Link>
                        <div className='icon-label'>Cosmetics</div>
                    </div>

                    <div className='icon-set'>
                        {/* Link to the Settings page */}
                        <Link to='/settings'>
                            <SettingsIcon className='menu-icon' />
                        </Link>
                        <div className='icon-label'>Settings</div>
                    </div>

                </div>
            </div>

            {/* Right side of the menu, containing the Exit button */}
            <div className='side'>

                <div className='exit-set'>
                    {/* Link to exit the menu */}
                    <ExitIcon className='exit-icon' onClick={handleExit} />
                    <div className='exit-label'>Exit</div>
                </div>

            </div>

        </div>
    )
}

// Exporting the MainMenu component, so that
// it can be imported and used as a component in
// a separate file
export default MainMenu;