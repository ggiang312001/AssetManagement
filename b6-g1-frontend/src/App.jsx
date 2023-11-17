import './App.css';
import React from 'react';
import { Route, Routes } from 'react-router-dom';
import Layout from './components/Layout/Layout';
import Login from './views/Login/Login';
import Home from './views/Home/Home';
import NotFound from './views/NotFound/NotFound';
import ManageUsers from './views/ManageUsers/ManageUsers';
import CreateNewUser from './views/CreateNewUser/CreateNewUser';
import CreateNewAsset from './views/CreateNewAsset/CreateNewAsset';
import AdminGuard from './components/Guard/AdminGuard';
import AuthGuard from './components/Guard/AuthGuard';
import EditAsset from './views/EditAsset/EditAsset';
import EditUser from './views/EditUser/EditUser';
import ManageAssets from './views/ManageAssets/ManageAssets';
import ManageAssignment from './views/ManageAssignment/ManageAssignment';
import FirstLoginGuard from './components/Guard/FirstLoginGuard';
import CreateNewAssignment from './views/CreateNewAssignment/CreateNewAssignment';
import ReturnRequests from './views/ReturnRequests/ReturnRequests';
import EditAssignment from './views/EditAssignment/EditAssignment';
import Report from './views/Report/Report';

const allRouters = [
	{
		path: '/home',
		element: (
			<AuthGuard>
				<FirstLoginGuard>
					<Home />
				</FirstLoginGuard>
			</AuthGuard>
		),
		title: 'Home',
	},
	{
		path: '/manage-assets',
		element: (
			<AdminGuard>
				<FirstLoginGuard>
					<ManageAssets />
				</FirstLoginGuard>
			</AdminGuard>
		),
		title: 'Manage Assets',
	},
	{
		path: '/manage-assets/edit-asset/:assetId',
		element: (
			<AdminGuard>
				<FirstLoginGuard>
					<EditAsset />
				</FirstLoginGuard>
			</AdminGuard>
		),
		title: 'Manage Assets > Edit Asset',
	},
	{
		path: '/manage-assets/create-asset',
		element: (
			<AdminGuard>
				<FirstLoginGuard>
					<CreateNewAsset />
				</FirstLoginGuard>
			</AdminGuard>
		),
		title: 'Manage Assets > Create New Asset',
	},
	{
		path: '/manage-users',
		element: (
			<AdminGuard>
				<FirstLoginGuard>
					<ManageUsers />
				</FirstLoginGuard>
			</AdminGuard>
		),
		title: 'Manage Users',
	},
	{
		path: '/manage-users/:staffCode/edit-user',
		element: (
			<AdminGuard>
				<FirstLoginGuard>
					<EditUser />
				</FirstLoginGuard>
			</AdminGuard>
		),
		title: 'Manage Users > Edit User',
	},
	{
		path: '/manage-users/create-user',
		element: (
			<AdminGuard>
				<FirstLoginGuard>
					<CreateNewUser />
				</FirstLoginGuard>
			</AdminGuard>
		),
		title: 'Manage Users > Create New User',
	},
	{
		path: '/manage-assignments',
		element: (
			<AdminGuard>
				<FirstLoginGuard>
					<ManageAssignment />
				</FirstLoginGuard>
			</AdminGuard>
		),
		title: 'Manage Assignment',
	},
	{
		path: '/manage-assignments/create-assignment',
		element: (
			<AdminGuard>
				<FirstLoginGuard>
					<CreateNewAssignment />
				</FirstLoginGuard>
			</AdminGuard>
		),
		title: 'Manage Assignment > Create New Assignment',
	},
	{
		path: '/manage-assignments/edit-assignment/:id',
		element: (
			<AdminGuard>
				<FirstLoginGuard>
					<EditAssignment />
				</FirstLoginGuard>
			</AdminGuard>
		),
		title: 'Manage Assignment > Edit Assignment',
	},
	{
		path: '/request-for-returning',
		element: (
			<AdminGuard>
				<FirstLoginGuard>
					<ReturnRequests />
				</FirstLoginGuard>
			</AdminGuard>
		),
		title: 'Request For Returning',
	},
	{
		path: '/report',
		element: (
			<AdminGuard>
				<FirstLoginGuard>
					<Report />
				</FirstLoginGuard>
			</AdminGuard>
		),
		title: 'Report',
	},
];

function App() {
	return (
		<Routes>
			{allRouters.map((item, index) => {
				const CommonLayout = item.layout || Layout;
				return (
					<Route
						key={index}
						path={item.path}
						element={
							<CommonLayout title={item.title} children={item.element} />
						}
					/>
				);
			})}
			<Route key='login' path='/' element={<Login />} />
			<Route key='*' path='*' element={<NotFound />} />
		</Routes>
	);
}

export default App;
