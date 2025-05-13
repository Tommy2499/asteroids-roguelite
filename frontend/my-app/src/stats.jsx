import React, { useState, useEffect } from 'react';
import './stats.css';
import { Link } from 'react-router-dom';
import TrophyIcon from './images/trophy-icon.svg?react';
import StarIcon from './images/star-icon.svg?react';
import ClockIcon from './images/clock-icon.svg?react';
import ControllerIcon from './images/controller-icon.svg?react';
import { useAuth } from './auth_context';
import { formatTime, formatLongNumber } from './utils.js';

function Stats() {
    const { user } = useAuth(); // Access the logged-in user's information
    const [highestScore, setHighestScore] = useState('0'); // State for the highest score
    const [highestLevel, setHighestLevel] = useState('0'); // State for the highest level
    const [longestSurvival, setLongestSurvival] = useState('00:00'); // State for the longest survival time
    const [gamesPlayed, setGamesPlayed] = useState('0'); // State for the total games played
    const username = user?.username ?? 'guest'; // Default to 'guest' if no user is logged in
    const profile_name = username; // Use the username as the profile name

    // Fetch the user's stats from the backend when the component is rendered
    useEffect(() => {
        fetch(`http://localhost:8080/api/getStats?username=${encodeURIComponent(username)}&profile_name=${encodeURIComponent(profile_name)}`)
            .then((response) => response.json())
            .then((data) => {
                console.log(`Success: ${data.success}, Error: ${data.error}`);
                const stats = data.stats;
                // Update the state with the fetched stats
                setHighestScore(formatLongNumber(stats.highest_score));
                setHighestLevel(formatLongNumber(stats.highest_level));
                setLongestSurvival(formatTime(stats.longest_duration));
                setGamesPlayed(formatLongNumber(stats.total_games));
                console.log(`Success: ${data.success}, Error: ${data.error}`)
            })
            .catch((err) => {
                console.error('Failed to fetch data: ', err); // Log any errors
            });
    });

    return (
        <div id='stats-page'>
            {/* Back button to return to the main menu */}
            <div id='back-container'>
                <Link to='/main_menu'>
                    <button className='button-2' id='stats-back-button'>{'< '}Back</button>
                </Link>
            </div>
                <div id='stats-title'>Stats</div>
            {/* Grid layout for displaying stats */}
            <div className='stats-grid'>
                {/* Highest Score */}
                <div className='stats-container' id='highest-score-container'>
                    <div className='stats-internal-top'>
                        <div className='stat-name'>Highest Score</div>
                        <div className='stats-top-right'><TrophyIcon/></div>
                    </div>
                    <div className='stat-value'>{highestScore}</div>
                </div>
                {/* Highest Level */}
                <div className='stats-container' id='highest-level-container'>
                    <div className='stats-internal-top'>
                        <div className='stat-name'>Highest Level</div>
                        <div className='stats-top-right'><StarIcon/></div>
                    </div>
                    <div className='stat-value'>{highestLevel}</div>
                </div>
                {/* Longest Survival */}
                <div className='stats-container' id='longest-survival-container'>
                    <div className='stats-internal-top'>
                        <div className='stat-name'>Longest Survival</div>
                        <div className='stats-top-right'><ClockIcon/></div>
                    </div>
                    <div className='stat-value'>{longestSurvival}</div>
                </div>
                {/* Games Played */}
                <div className='stats-container' id='games-played-container'>
                    <div className='stats-internal-top'>
                        <div className='stat-name'>Games Played</div>
                        <div className='stats-top-right'><ControllerIcon/></div>
                    </div>
                    <div className='stat-value'>{gamesPlayed}</div>
                </div>
            </div>
        </div>
    );
}

export default Stats;