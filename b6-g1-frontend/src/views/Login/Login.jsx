// @ts-nocheck
import React, { useState, useContext, useEffect } from 'react';
import {
	Button,
	Card,
	Col,
	Form,
	Input,
	Layout,
	message,
	Row,
	Spin,
} from 'antd';
import { login } from '../../services/authService';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../../contexts/AuthContext';
import { Content } from 'antd/lib/layout/layout';
import logo from '../../assets/images/nashtech.png';

const Login = () => {
	const [loading, setLoading] = useState(false);
	const {
		authContext: { isAuthenticated },
		setAuthContext,
	} = useContext(AuthContext);
	const navigate = useNavigate();
	const [form] = Form.useForm();
	const [, forceUpdate] = useState({});

	useEffect(() => {
		if (isAuthenticated) {
			navigate('/home');
		}
	});

	// To disable submit button at the beginning.
	useEffect(() => {
		forceUpdate({});
	}, []);

	const onFinish = async (values) => {
		setLoading(true);
		login(values)
			.then(({ data: { accessToken, role, isFirstLogin } }) => {
				localStorage.setItem('token', accessToken);
				localStorage.setItem('username', values.username);
				localStorage.setItem('role', role);
				localStorage.setItem('isFirstLogin', isFirstLogin);

				const auth = {
					isAuthenticated: true,
					username: values.username,
					isFirstLogin,
					role,
				};

				setAuthContext(auth);
				message.success('Logged in successfully!', 1);

				setTimeout(() => {
					navigate('/home');
				}, 1000);
			})
			.catch((error) => {
				message.error(error.message);
			});
		setLoading(false);
	};

	return (
		<Layout>
			<Content style={{ height: '100vh' }}>
				<Row justify='space-around' align='middle' style={{ height: '100%' }}>
					<Col span={12} offset={4}>
						<Card style={{ maxWidth: '450px' }}>
							<Row
								justify='center'
								align='middle'
								style={{ marginBottom: '30px' }}>
								<img
									alt='nashtech-logo'
									src={logo}
									style={{ width: '100px' }}
								/>
							</Row>
							<Row
								justify='center'
								align='middle'
								style={{ marginBottom: '30px' }}>
								<h1 style={{ fontSize: '25px' }}>Online Asset Management</h1>
							</Row>
							<Form
								name='login'
								form={form}
								onFinish={onFinish}
								labelAlign='left'>
								<Form.Item
									name='username'
									label='Username'
									rules={[
										{
											required: true,
											message: 'Please input your username!',
										},
										{
											min: 2,
											message: 'The length of username must be 2-64 characters',
										},
										{
											max: 64,
											message: 'The length of username must be 2-64 characters',
										},
									]}>
									<Input placeholder='Username' />
								</Form.Item>
								<Form.Item
									label='Password'
									name='password'
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
									<Input.Password placeholder='Password' />
								</Form.Item>
								{loading ? (
									<Spin />
								) : (
									<Form.Item shouldUpdate style={{ textAlign: 'center' }}>
										{() => (
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
												Log in
											</Button>
										)}
									</Form.Item>
								)}
							</Form>
						</Card>
					</Col>
				</Row>
			</Content>
		</Layout>
	);
};

export default Login;
