import React, { createContext, useState, useContext } from 'react';

const SharedIdContext = createContext();

export const SharedIdProvider = ({ children }) => {
  const [sharedId, setSharedId] = useState('');

  return (
    <SharedIdContext.Provider value={{ sharedId, setSharedId }}>
      {children}
    </SharedIdContext.Provider>
  );
};

export const useSharedId = () => {
  const context = useContext(SharedIdContext);
  if (!context) {
    throw new Error('useSharedId must be used within a SharedIdProvider');
  }
  return context;
};
