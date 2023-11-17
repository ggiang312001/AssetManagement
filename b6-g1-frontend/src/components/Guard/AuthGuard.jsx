import React, { useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../contexts/AuthContext';

const AuthGuard = ({ children }) => {
	const {
		authContext: { isAuthenticated },
	} = useContext(AuthContext);
	const navigate = useNavigate();

	useEffect(() => {
		if (!isAuthenticated) {
			navigate('/');
		}
	}, [navigate, isAuthenticated]);
	return <div>{children}</div>;
};

export default AuthGuard;
