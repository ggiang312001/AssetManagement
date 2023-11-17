import { Button, Form, Input, message, Modal, Space, Spin } from 'antd';
import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../contexts/AuthContext';
import { changePassword, logout } from '../../services/authService';

const ChangePassword = (props) => {
	const { isFirstLogin, changePasswordModalOpen, setChangePasswordModalOpen } =
		props;
	const [loading, setLoading] = useState(false);
	const { setAuthContext } = useContext(AuthContext);
	const navigate = useNavigate();

	const [form] = Form.useForm();
	const [, forceUpdate] = useState({});

	useEffect(() => {
		forceUpdate({});
	}, []);

	const onFinishFirstLogin = (values) => {
		setLoading(true);

		changePassword({ oldPassword: null, newPassword: values.newPassword })
			.then(() => {
				message.success(
					'Changed password successfully! Please log in again with your new password',
				);
				logout();
				navigate('/');
				setAuthContext({
					isAuthenticated: false,
					username: null,
					isFirstLogin: null,
					role: null,
				});
			})
			.catch((error) => {
				message.error(error.message);
			});
		setLoading(false);
	};

	const onFinish = (values) => {
		setLoading(true);

		changePassword(values)
			.then(() => {
				message.success(
					'Changed password successfully! Please log in again with your new password',
				);
				logout();
				navigate('/');
				setAuthContext({
					isAuthenticated: false,
					username: null,
					isFirstLogin: null,
					role: null,
				});
			})
			.catch((error) => {
				message.error(error.message);
			});
		setLoading(false);
	};

	const handleCancel = () => setChangePasswordModalOpen(false);

	return (
		<>
			<Modal
				visible={typeof isFirstLogin === 'boolean' && isFirstLogin}
				title='Change Password'
				footer={null}
				closable={false}
				keyboard={false}>
				<p>
					This is the first time you logged in. <br />
					You have to change your password to continue.
				</p>
				<Form
					form={form}
					layout='horizontal'
					name='change-password-first-time'
					onFinish={onFinishFirstLogin}>
					<Form.Item
						name='newPassword'
						label='New Password'
						rules={[
							{
								required: true,
								message: 'Please input your password!',
							},
							{
								min: 8,
								message: 'The length of password must be 8-64 characters',
							},
							{
								max: 64,
								message: 'The length of password must be 8-64 characters',
							},
						]}>
						<Input.Password placeholder='New Password' />
					</Form.Item>
					<Form.Item shouldUpdate style={{ textAlign: 'right' }}>
						{() => (
							<Button
								type='primary'
								htmlType='submit'
								danger
								disabled={
									!form.isFieldsTouched(true) ||
									!!form.getFieldsError().filter(({ errors }) => errors.length)
										.length
								}>
								{loading ? <Spin /> : 'Save'}
							</Button>
						)}
					</Form.Item>
				</Form>
			</Modal>
			<Modal
				visible={changePasswordModalOpen}
				title='Change Password'
				footer={null}
				onCancel={handleCancel}>
				<Form
					form={form}
					layout='horizontal'
					name='change-password'
					onFinish={onFinish}>
					<Form.Item
						name='oldPassword'
						label='Old Password'
						rules={[
							{
								required: true,
								message: 'Please input your old password!',
							},
							{
								min: 8,
								message: 'The length of password must be 8-64 characters',
							},
							{
								max: 64,
								message: 'The length of password must be 8-64 characters',
							},
						]}>
						<Input.Password  placeholder='Old Password' />
					</Form.Item>
					<Form.Item
						name='newPassword'
						label='New Password'
						rules={[
							{
								required: true,
								message: 'Please input your password!',
							},
							{
								min: 8,
								message: 'The length of password must be 8-64 characters',
							},
							{
								max: 64,
								message: 'The length of password must be 8-64 characters',
							},
						]}>
						<Input.Password placeholder='New Password' />
					</Form.Item>
					<Form.Item shouldUpdate style={{ textAlign: 'right' }}>
						{() => (
							<Space size={5}>
								<Button
									type='primary'
									htmlType='submit'
									danger
									disabled={
										!form.isFieldsTouched(true) ||
										!!form
											.getFieldsError()
											.filter(({ errors }) => errors.length).length
									}>
									{loading ? <Spin /> : 'Save'}
								</Button>
								<Button onClick={handleCancel}>Cancel</Button>
							</Space>
						)}
					</Form.Item>
				</Form>
			</Modal>
		</>
	);
};

export default ChangePassword;
