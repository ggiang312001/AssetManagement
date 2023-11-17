// @ts-nocheck
import React, { useContext, useState } from 'react';
import { Header as HeaderAntd } from 'antd/lib/layout/layout';
import { Link } from 'react-router-dom';
import { Menu } from 'antd';
import { AuthContext } from '../../contexts/AuthContext';
import { CaretDownOutlined } from '@ant-design/icons';
import ChangePassword from '../Modal/ChangePassword';
import LogOutConfirm from '../Modal/LogOutConfirm';

const Header = ({ title, ...otherProps }) => {
	const [changePasswordModalOpen, setChangePasswordModalOpen] = useState(false);
	const [logOutModalOpen, setLogOutModalOpen] = useState(false);

	const {
		authContext: { username },
	} = useContext(AuthContext);

	const navItems = [
		{
			label: (
				<>
					{username} <CaretDownOutlined />
				</>
			),
			key: 'username',
			children: [
				{
					key: 'change-password',
					label: (
						<Link
							onClick={() => {
								setChangePasswordModalOpen(true);
							}}>
							Change Password
						</Link>
					),
				},
				{
					key: 'log-out',
					label: <Link onClick={() => setLogOutModalOpen(true)}>Logout</Link>,
				},
			],
		},
	];

	return (
		<>
			<ChangePassword
				changePasswordModalOpen={changePasswordModalOpen}
				setChangePasswordModalOpen={setChangePasswordModalOpen}
			/>
			<LogOutConfirm
				logOutModalOpen={logOutModalOpen}
				setLogOutModalOpen={setLogOutModalOpen}
			/>
			<HeaderAntd>
				<div className='header'>
					<div>{title}</div>
					<Menu mode='horizontal' items={navItems} disabledOverflow></Menu>
				</div>
			</HeaderAntd>
		</>
	);
};

export default Header;
