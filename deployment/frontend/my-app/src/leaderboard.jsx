import React, { useState, useEffect } from 'react';
import './leaderboard.css';
import { Link } from 'react-router-dom';
import { formatLongNumber } from './utils.js'

function Leaderboard() {

    const [tab, setTab] = useState('score');
    const [leaders, setLeaders] = useState([]);
    const [limit, setLimit] = useState(10);
    const [difficulty, setDifficulty] = useState('ALL');

    useEffect(() => {
        fetch(`http://137.184.232.147:5000/api/leaderboard?limit=${limit}&score=${tab}&difficulty=${difficulty}`)
        .then((response) => response.json())
        .then((data) => {
            let newLeaders = [];
            for (let leader of data.leaderboard){
                leader.favorite_ship = '/asteroid-logo-bgless.png';
                if (tab == 'level'){
                    leader.score = leader.level;
                }
                leader.score = formatLongNumber(leader.score);
                newLeaders.push(leader);
            }
            setLeaders(newLeaders);
            console.log(`Success: ${data.success}, Error: ${data.error}`)
        })
        .catch((err) => {
            console.error('Failed to fetch data: ', err);
        });
    }, [tab])

    return (
        <div id='leaderboards-page'>
            <div id='leaderboards-content'>
                <div id='leaderboard-back-wrapper'>
                    <Link to='/main_menu'>
                        <button className='button-2' id='leaderboard-back'>{'< '}Back</button>
                    </Link>
                </div>
                <div id='leaderboards-title'>Leaderboards</div>
                <div className='tabs'>
                    <button className={tab === 'score' ? 'button-2 active' : 'button-2'} id='scores-button' onClick = {() => {setTab('score')}}>Score</button>
                    <button className={tab === 'level' ? 'button-2 active' : 'button-2'} id='levels-button' onClick = {() => {setTab('level')}}>Level</button>
                </div>
                <div id='leaderboard-container'>
                    <div id='leaderboard-stack'>
                        {leaders.map((data, index) => (
                            <div className='leaderboard-item'>
                                <div className='rank'>{index+1}</div>
                                <div className='player' key={index}>
                                    <img src={data.favorite_ship} alt='ship' className='favorite-ship'/>
                                    <div className='leaderboard-username'>{data.user}</div>
                                    <div className='leaderboard-score'>{data.score}</div>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div id='leaderboard-buffer'></div>
                </div>
            </div>
        </div>
    );
}

export default Leaderboard;