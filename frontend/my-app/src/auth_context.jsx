import {createContext, useContext, useState} from 'react'

const AuthContext = createContext(null);

// Wrapper component that provides context to any child inside of children
export const AuthProvider = ({children}) => {

    // null indicates user is not logged in
    const [user, setUser] = useState(null);

    // sets user data as a state when the user logs in
    // when logging in, we pass user data, which indicates 
    const login = (userData) => setUser(userData);
    // clears user state by setting user to null (not logged in state)
    const logout = () => setUser(null);

    return (
        // Every child inside children can access this AuthContext
        // Meaning, they have access to the user, as well as logging a user in and out
        <AuthContext.Provider value={{user, login, logout}}>
            {children}
        </AuthContext.Provider>
    );

};

// Custom hook to make it easy to access auth state anywhere
// We can call useAuth to access state
// Without this line, we would have to use useContext(AuthContext)
export const useAuth = () => useContext(AuthContext);