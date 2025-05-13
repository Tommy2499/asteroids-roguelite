import { StrictMode } from 'react'; // Import StrictMode for highlighting potential problems
import { createRoot } from 'react-dom/client'; // Import createRoot for rendering the app
import './index.css'; // Import global styles
import App from './App.jsx'; // Import the root App component

// Render the root App component into the DOM
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App /> {/* Wrap the App component in StrictMode for additional checks */}
  </StrictMode>,
);
