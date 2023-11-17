// @ts-nocheck
import { Menu } from 'antd';
import React, { useContext } from 'react';
import { Link, useLocation } from 'react-router-dom';
import logo from '../../assets/images/nashtech.png';
import { AuthContext } from '../../contexts/AuthContext';

const Sidebar = () => {
	const location = useLocation();

	const {
		authContext: { role },
	} = useContext(AuthContext);

	const staffItems = [
		{
			label: <Link to='/home'>Home</Link>,
			key: 'home',
		},
	];

	const adminItems = [
		{
			label: <Link to='/home'>Home</Link>,
			key: 'home',
		},
		{
			label: <Link to='/manage-users'>Manage Users</Link>,
			key: 'manage-users',
		},
		{
			label: <Link to='/manage-assets'>Manage Assets</Link>,
			key: 'manage-assets',
		},
		{
			label: <Link to='/manage-assignments'>Manage Assignments</Link>,
			key: 'manage-assignments',
		},
		{
			label: <Link to='/request-for-returning'>Request For Returning</Link>,
			key: 'request-for-returning',
		},
		{
			label: <Link to='/report'>Report</Link>,
			key: 'report',
		},
	];

	return (
		<div>
			<div
				style={{
					textAlign: 'center',
					marginTop: '20px',
					marginBottom: '30px',
				}}>
				<img
					alt='nashtech-logo'
					src={logo}
					style={{ width: '150px', paddingTop: '30px' }}
				/>
				<h2>Online Asset Management</h2>
			</div>
			<Menu
				style={{
					width: '100%',
				}}
				defaultSelectedKeys={[location.pathname.split('/')[1]]}
				mode='inline'
				items={role === 'ADMIN' ? adminItems : staffItems}
			/>
		</div>
	);
};

export default Sidebar;
