import { Button, DatePicker, Form, Input, Radio, Select, message } from 'antd';
import React, { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import moment from 'moment';
import { getUserByStaffCode, editUser } from '../../services/userService';

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

const EditUser = () => {
	const navigate = useNavigate();
	const [form] = Form.useForm();

	const { staffCode } = useParams();
	const [detailUser, setDetailUser] = useState({
		firstName: '',
		lastName: '',
		birthDate: '',
		gender: '',
		createdAt: '',
		role: '',
	});

	const getDetail = useCallback(() => {
		getUserByStaffCode(staffCode)
			.then((res) => {
				const { firstName, lastName, birthDate, gender, createdAt, role } =
					res.data;
				setDetailUser({
					firstName,
					lastName,
					birthDate,
					gender,
					createdAt,
					role,
				});
			})
			.catch((error) => {
				message.error(error.message);
			});
	}, [staffCode]);

	useEffect(() => {
		if (staffCode) {
			getDetail();
		}
	}, [getDetail, staffCode]);
	const onFinish = (values) => {
		const data = {
			firstName: values.firstName,
			lastName: values.lastName,
			birthDate: DateFormat(values.birthDate),
			gender: values.gender,
			createdAt: DateFormat(values.joinedDate),
			role: values.type,
		};
		editUser(staffCode, data)
			.then((res) => {
				message.success('User is edited successfully');
				navigate('/manage-users');
			})
			.catch((err) => {
				message.error(err.message);
			});
	};

	return (
		<>
			<h1>Edit User</h1>
			<Form
				form={form}
				name='edit-asset'
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
				onFinish={onFinish}
				fields={[
					{
						name: 'firstName',
						value: detailUser.firstName,
					},
					{
						name: 'lastName',
						value: detailUser.lastName,
					},
					{
						name: 'birthDate',
						value: moment(detailUser.birthDate),
					},
					{
						name: 'gender',
						value: detailUser.gender,
					},
					{
						name: 'joinedDate',
						value: moment(detailUser.createdAt),
					},
					{
						name: 'type',
						value: detailUser.role,
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
					<Input placeholder='First Name' maxLength={255} disabled={true} />
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
					<Input placeholder='Last Name' maxLength={255} disabled={true} />
				</Form.Item>

				<Form.Item
					name='birthDate'
					label='Date of Birth'
					rules={[
						{
							required: true,
							message: 'Date of birth is required',
						},
					]}>
					<DatePicker
						format='YYYY/MM/DD'
						inputReadOnly={true}
						disabledDate={(d) =>
							!d ||
							d.isAfter(
								(new Date().getFullYear() - 18).toString() +
									'-' +
									(new Date().getMonth() + 1).toString() +
									'-' +
									(new Date().getDate() - 1).toString(),
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
						format='YYYY/MM/DD'
						inputReadOnly={true}
						disabledDate={(d) =>
							moment(d).day() === 0 ||
							moment(d).day() === 6 ||
							d.isBefore(
								(
									new Date(form.getFieldValue('birthDate')).getFullYear() + 18
								).toString() +
									'-' +
									(
										new Date(form.getFieldValue('birthDate')).getMonth() + 1
									).toString() +
									'-' +
									(
										new Date(form.getFieldValue('birthDate')).getDate() + 1
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
									form.getFieldValue('birthDate') === undefined ||
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
		</>
	);
};

export default EditUser;
