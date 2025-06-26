import React, { useState, useEffect } from 'react';
import './leaderboard.css';
import { Link } from 'react-router-dom';
import { formatLongNumber } from './utils.js'; // Utility function to format large numbers
import asteroidLogo from './images/asteroid-logo-bgless.png';

function Leaderboard() {
    // State to track the active tab (score or level)
    const [tab, setTab] = useState('score');
    // State to store the leaderboard data
    const [leaders, setLeaders] = useState([]);
    // State to set the number of leaderboard entries to fetch
    const [limit, setLimit] = useState(10);
    // State to filter leaderboard by difficulty
    const [difficulty, setDifficulty] = useState('ALL');

    // Fetch leaderboard data whenever the tab changes
    useEffect(() => {
        fetch(`http://137.184.232.147:5000/api/leaderboard?limit=${limit}&score=${tab}&difficulty=${difficulty}`)
        .then((response) => response.json())
        .then((data) => {
            let newLeaders = [];
            for (let leader of data.leaderboard) {
                // Assign a placeholder image for the favorite ship
                leader.favorite_ship = asteroidLogo;
                // If the tab is 'level', use the level as the score
                if (tab === 'level') {
                    leader.score = leader.level;
                }
                // Format the score for better readability
                leader.score = formatLongNumber(leader.score);
                newLeaders.push(leader);
            }
            setLeaders(newLeaders); // Update the leaderboard state
            console.log(`Success: ${data.success}, Error: ${data.error}`);
        })
        .catch((err) => {
            console.error('Failed to fetch data: ', err); // Log any errors
        });
    }, [tab]); // Dependency array ensures this runs when the tab changes

    return (
        <div id='leaderboards-page'>
            {/* Main content of the leaderboard page */}
            <div id='leaderboards-content'>
                {/* Back button to return to the main menu */}
                <div id='leaderboard-back-wrapper'>
                    <Link to='/main_menu'>
                        <button className='button-2' id='leaderboard-back'>{'< '}Back</button>
                    </Link>
                </div>
                <div id='leaderboards-title'>Leaderboards</div>
                {/* Tabs to switch between score and level leaderboards */}
                <div className='tabs'>
                    <button className={tab === 'score' ? 'button-2 active' : 'button-2'} id='scores-button' onClick = {() => {setTab('score')}}>Score</button>
                    <button className={tab === 'level' ? 'button-2 active' : 'button-2'} id='levels-button' onClick = {() => {setTab('level')}}>Level</button>
                </div>
                {/* Container for the leaderboard entries */}
                <div id='leaderboard-container'>
                    <div id='leaderboard-stack'>
                        {/* Map through the leaderboard data and render each entry */}
                        {leaders.map((data, index) => (
                            <div className='leaderboard-item' key={index}>
                                <div className='rank'>{index+1}</div>
                                    <div className='player'>
                                    <img src={data.favorite_ship} alt='ship' className='favorite-ship'/>
                                    <div className='leaderboard-username'>{data.user}</div>
                                    <div className='leaderboard-score'>{data.score}</div>
                                </div>
                            </div>
                        ))}
                    </div>
                    {/* Buffer for spacing */}
                    <div id='leaderboard-buffer'></div>
                </div>
            </div>
        </div>
    );
}

export default Leaderboard;