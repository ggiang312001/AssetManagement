import React from 'react';
import ChangePassword from '../Modal/ChangePassword';

const FirstLoginGuard = ({ children }) => {
	const isFirstLogin = localStorage.getItem('isFirstLogin') === 'true';

	return (
		<>
			<ChangePassword isFirstLogin={isFirstLogin} />
			{children}
		</>
	);
};

export default FirstLoginGuard;
