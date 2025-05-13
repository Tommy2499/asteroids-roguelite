import React from 'react';
import InDevelopment from './in_development.jsx';

function Profiles() {
    const description = 'The Profiles page will include creating, modifying, and deleting profiles. Each profile operates like a save file. They will have their own set of stats and records.';
    return (
        <div id='profiles-container'>
            <InDevelopment page='Profiles' description={description}/>
        </div>
    );
}

export default Profiles;