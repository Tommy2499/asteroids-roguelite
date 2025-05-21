// Import React to export xml
import React from 'react'
// Browser Router enables setting up page routes
import {BrowserRouter, Routes, Route} from 'react-router-dom';
import { AuthProvider } from './auth_context';
import ProtectedRoute from './protected_route';

// Highest specificity stylesheet
import './App.css';

// Files we will use as page Routes
import Home from './home';
import MainMenu from './main_menu';
import Profiles from './profiles';
import Stats from './stats';
import Leaderboard from './leaderboard';
import Cosmetics from './cosmetics';
import Settings from './settings';
import Play from './play';

/**
 * App component sets up the routing for the application using React Router.
 * It defines the routes for various pages.
 *
 * @returns {JSX.Element} The JSX structure for the application's routing.
 */
function App() {
  return (
    <AuthProvider>
    {/** Pages to render here */}
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Home/>}/>
          <Route path='/main_menu' element={<ProtectedRoute><MainMenu/></ProtectedRoute>}/>
          <Route path='/profiles' element={<ProtectedRoute><Profiles/></ProtectedRoute>}/>
          <Route path='/stats' element={<ProtectedRoute><Stats/></ProtectedRoute>}/>
          <Route path='/leaderboard' element={<ProtectedRoute><Leaderboard/></ProtectedRoute>}/>
          <Route path='/cosmetics' element={<ProtectedRoute><Cosmetics/></ProtectedRoute>}/>
          <Route path='/settings' element={<ProtectedRoute><Settings/></ProtectedRoute>}/>
          <Route path='/play' element={<ProtectedRoute><Play/></ProtectedRoute>}/>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
};

// Exporting the App component, so that
// it can be imported and rendered as the root
// of the DOM in main
export default App;
