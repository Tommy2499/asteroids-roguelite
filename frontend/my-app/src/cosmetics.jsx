import React from 'react';
import InDevelopment from './in_development.jsx';

function Cosmetics() {
    const description = 'The Cosmetics page will allow users to choose different sprites for the ship, enemies, and bullets.'
    return (
        <div id='cosmetics-container'>
            <InDevelopment page='Cosmetics' description={description} />
        </div>
    );
}

export default Cosmetics;