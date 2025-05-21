import React from 'react';
import InDevelopment from './in_development.jsx';

function Settings() {
    const description = 'The Settings page will allow users to toggle different options. Some of these may include sound options, light/dark mode, username/password configuration, and user deletion.';
    return (
        <div id='settings-container'>
            <InDevelopment page='Settings' description={description} />
        </div>
    );
}

export default Settings;