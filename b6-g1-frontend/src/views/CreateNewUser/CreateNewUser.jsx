import {
	Button,
	DatePicker,
	Form,
	Input,
	Radio,
	Select,
	message,
	Typography,
} from 'antd';
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { createUser } from '../../services/userService';
import moment from 'moment';

function DateFormat(date) {
	let Day;
	if (new Date(date).getDate() < 10) {
		Day = '0' + new Date(date).getDate();
	} else {
		Day = new Date(date).getDate();
	}
	let Month;
	if (new Date(date).getMonth() < 10) {
		Month = '0' + (new Date(date).getMonth() + 1);
	} else {
		Month = new Date(date).getMonth() + 1;
	}
	const Year = new Date(date).getFullYear();
	const newDateFormat = `${Year}-${Month}-${Day}`;
	return newDateFormat;
}

const CreateNewUser = () => {
	const navigate = useNavigate();
	const [form] = Form.useForm();
	const onFinish = (values) => {
		const data = {
			firstName: values.firstName,
			lastName: values.lastName,
			birthDate: DateFormat(values.birthday),
			gender: values.gender,
			createdAt: DateFormat(values.joinedDate),
			role: values.type,
		};
		createUser(data)
			.then((res) => {
				message.success('User is created successfully');
				navigate('/manage-users');
			})
			.catch((err) => {
				message.error(err.message);
			});
	};
	const [, forceUpdate] = useState({});

	useEffect(() => {
		forceUpdate({});
	}, []);
	return (
		<div className='container-fluid' style={{ padding: '25px' }}>
			<Typography.Title id='create-user-form-title' level={2} strong>
				Create new user
			</Typography.Title>
			<Form
				form={form}
				name='create-new-user'
				onFinish={onFinish}
				scrollToFirstError
				labelCol={{
					span: 4,
				}}
				wrapperCol={{
					span: 14,
				}}
				labelAlign='left'
				labelWrap
				style={{
					margin: '40px 0',
				}}
				layout='horizontal'
				fields={[
					{
						name: 'gender',
						value: 'FEMALE',
					},
				]}>
				<Form.Item
					name='firstName'
					label='First Name'
					rules={[
						{
							whitespace: true,
							message: 'First name is required',
						},
						{
							pattern: new RegExp(/\b([A-Z][a-z]+[ ]*)+$/),
							message:
								'First name should contain first upper case characters & the other lower case characters',
						},
						{
							required: true,
							message: 'First name is required',
						},
						{
							max: 50,
							message: 'The length of name must be max 50 characters',
						},
					]}>
					<Input placeholder='First Name' maxLength={255} />
				</Form.Item>

				<Form.Item
					name='lastName'
					label='Last Name'
					rules={[
						{
							whitespace: true,
							message: 'Last name is required',
						},
						{
							pattern: new RegExp(/\b([A-Z][a-z]+[ ]*)+$/),
							message:
								'Last name should contain first upper case characters & the other lower case characters',
						},
						{
							required: true,
							message: 'Please provide Last name',
						},
						{
							max: 50,
							message: 'The length of name must be max 50 characters',
						},
					]}>
					<Input placeholder='Last Name' maxLength={255} />
				</Form.Item>

				<Form.Item
					name='birthday'
					label='Date of Birth'
					rules={[
						{
							required: true,
							message: 'Date of birth is required',
						},
					]}>
					<DatePicker
						minDate={new Date(2010, 11, 12)}
						inputReadOnly={true}
						disabledDate={(d) =>
							!d ||
							d.isAfter(
								(new Date().getFullYear() - 18).toString() +
									'-' +
									(new Date().getMonth() + 1).toString() +
									'-' +
									new Date().getDate().toString(),
							)
						}
					/>
				</Form.Item>

				<Form.Item
					name='gender'
					label='Gender'
					rules={[
						{
							required: true,
							message: 'Gender is required',
						},
					]}>
					<Radio.Group defaultValue='FEMALE'>
						<Radio value='MALE'>Male</Radio>
						<Radio value='FEMALE'>Female</Radio>
					</Radio.Group>
				</Form.Item>

				<Form.Item
					name='joinedDate'
					label='Joined Date'
					rules={[
						{
							required: true,
							message: 'Joined date is required',
						},
					]}>
					<DatePicker
						inputReadOnly={true}
						disabledDate={(d) =>
							moment(d).day() === 0 ||
							moment(d).day() === 6 ||
							d.isBefore(
								(
									new Date(form.getFieldValue('birthday')).getFullYear() + 18
								).toString() +
									'-' +
									(
										new Date(form.getFieldValue('birthday')).getMonth() + 1
									).toString() +
									'-' +
									(
										new Date(form.getFieldValue('birthday')).getDate() + 1
									).toString(),
							) ||
							d.isAfter(
								new Date().getFullYear().toString() +
									'-' +
									(new Date().getMonth() + 1).toString() +
									'-' +
									(new Date().getDate() + 1).toString(),
							)
						}
					/>
				</Form.Item>

				<Form.Item
					name='type'
					label='Type'
					rules={[
						{
							required: true,
							message: 'Type is required',
						},
					]}>
					<Select placeholder='Type'>
						<Select.Option value='ADMIN'>Admin</Select.Option>
						<Select.Option value='STAFF'>Staff</Select.Option>
					</Select>
				</Form.Item>

				<Form.Item
					shouldUpdate
					wrapperCol={{
						offset: 4,
						span: 16,
					}}
					style={{ justify_item: 'right' }}>
					{() => (
						<>
							<Button
								type='primary'
								danger
								htmlType='submit'
								disabled={
									form.getFieldValue('firstName') === undefined ||
									form.getFieldValue('lastName') === undefined ||
									form.getFieldValue('birthday') === undefined ||
									form.getFieldValue('gender') === undefined ||
									form.getFieldValue('joinedDate') === undefined ||
									form.getFieldValue('type') === undefined ||
									!!form.getFieldsError().filter(({ errors }) => errors.length)
										.length
								}
								style={{ marginRight: '5px' }}>
								Save
							</Button>
							<Button
								onClick={() => navigate('/manage-users')}
								style={{ marginLeft: '5px' }}>
								Cancel
							</Button>
						</>
					)}
				</Form.Item>
			</Form>
		</div>
	);
};

export default CreateNewUser;
