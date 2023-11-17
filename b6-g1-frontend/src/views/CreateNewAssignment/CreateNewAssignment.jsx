import React, { useEffect, useState } from 'react';

import { useNavigate } from 'react-router-dom';

import {
	Button,
	DatePicker,
	Form,
	Input,
	Select,
	message,
	Typography,
} from 'antd';
import { SearchOutlined } from '@ant-design/icons';
import { getUserList } from '../../services/userService';
import { getAssets } from '../../services/assetService';
import { createAssignment } from '../../services/assignmentService';
import moment from 'moment';

const CreateNewAssignment = () => {
	const navigate = useNavigate();
	const [userResults, setUserResults] = useState(undefined);
	const [assets, setAssets] = useState(undefined);
	const sort = 'sort=staffCode,asc';
	const pageSize = 10;
	const pageNo = 1;
	const userRole = '';

	const assetSortBy = 'assetId';
	const assetSortDir = 'asc';
	const assetCategory = 0;
	const assetState = 'AVAILABLE';
	const [form] = Form.useForm();

	useEffect(() => {
		getAssets({
			searchTerm: '',
			cateFill: assetCategory,
			stateFill: assetState,
			pageSize: pageSize,
			pageNo: pageNo,
			sortBy: assetSortBy,
			sortDir: assetSortDir,
		})
			.then((res) => {
				setAssets(res.data.content);
			})
			.catch((err) => {
				message.error(err.message);
			});
	}, []);

	useEffect(() => {
		getUserList({
			pageSize: pageSize,
			pageNo: pageNo,
			searchTerm: '',
			type: userRole,
			sort: sort,
		})
			.then((res) => {
				setUserResults(res.data.content);
			})
			.catch((err) => {
				message.error(err.message);
			});
	}, [userRole]);

	const defaultDate = new Date().toISOString().split('T')[0];

	const onFinish = (values) => {
		const data = {
			assignee: values.assignee,
			assetId: values.assetId,
			assignDate: values.assignDate.format('YYYY-MM-DD'),
			assignNote: values.assignNote,
			state: 'WAITING_FOR_ACCEPTANCE',
		};
		createAssignment(data)
			.then((res) => {
				message.success('Assignment is created successfully');
				navigate('/manage-assignments');
			})
			.catch((err) => {
				message.error(err.message);
			});
	};

	const handleSearch = (input) => {
		getUserList({
			pageSize: pageSize,
			pageNo: pageNo,
			searchTerm: input,
			type: userRole,
			sort: sort,
		})
			.then((res) => {
				setUserResults(res.data.content);
			})
			.catch((err) => {
				message.error(err.message);
			});
	};

	const handleAssetSearch = (input) => {
		getAssets({
			searchTerm: input,
			cateFill: assetCategory,
			stateFill: assetState,
			pageSize: pageSize,
			pageNo: pageNo,
			sortBy: assetSortBy,
			sortDir: assetSortDir,
		})
			.then((res) => {
				setAssets(res.data.content);
			})
			.catch((err) => {
				message.error(err.message);
			});
	};
	return (
		<div className='container-fluid' style={{ padding: '50px' }}>
			<Typography.Title id='create-asset-form-title' level={2} strong>
				Create new assignment
			</Typography.Title>
			<Form
				form={form}
				name='create-new-assignment'
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
						name: 'assignDate',
						value: moment(defaultDate, 'YYYY-MM-DD'),
					},
				]}>
				<Form.Item
					name='assignee'
					label='User'
					rules={[
						{
							required: true,
							message: 'User is required',
						},
					]}>
					<Select
						showSearch
						// value={userValue}
						placeholder='Enter search keyword'
						defaultActiveFirstOption={true}
						filterOption={false}
						onSearch={handleSearch}
						suffixIcon={<SearchOutlined />}
						// onChange={(value) => setUserValue(value)}
						notFoundContent={
							<Typography.Text type='secondary'>
								 User not found
							</Typography.Text>
						}>
						{userResults &&
							userResults.map((user) => (
								<>
									<Select.Option value={user.staffCode}>
										<Typography.Text>
											{user.firstName + ' ' + user.lastName}
										</Typography.Text>
										<br />
										<Typography.Text type='secondary'>
											{user.username + ' - ' + user.staffCode}
										</Typography.Text>
									</Select.Option>{' '}
								</>
							))}
					</Select>
				</Form.Item>

				<Form.Item
					name='assetId'
					label='Asset'
					rules={[
						{
							required: true,
							message: 'Asset is required',
						},
					]}>
					<Select
						showSearch
						// value={userValue}
						placeholder='Enter search keyword'
						defaultActiveFirstOption={true}
						filterOption={false}
						onSearch={handleAssetSearch}
						suffixIcon={<SearchOutlined />}
						// onChange={(value) => setUserValue(value)}
						notFoundContent={
							<Typography.Text type='secondary'>
								Asset not found
							</Typography.Text>
						}>
						{assets &&
							assets.map((asset) => (
								<>
									<Select.Option value={asset.assetId}>
										<Typography.Text>{asset.name}</Typography.Text>
										<br />
										<Typography.Text type='secondary'>
											{asset.assetId}
										</Typography.Text>
									</Select.Option>{' '}
								</>
							))}
					</Select>
				</Form.Item>

				<Form.Item
					name='assignDate'
					label='Assigned Date'
					rules={[
						{
							required: true,
							message: 'Assigned date is required',
						},
					]}
					defaultValue={defaultDate}>
					<DatePicker
						inputReadOnly={true}
						defaultValue={moment(defaultDate, 'YYYY-MM-DD')}
						format='YYYY-MM-DD'
						disabledDate={(d) =>
							d.isSameOrBefore(new Date().setDate(new Date().getDate() - 1))
						}
					/>
				</Form.Item>

				<Form.Item
					name='assignNote'
					label={<label className='create-assignment-label'> Note </label>}
					id='assignNote'
					rules={[]}>
					<Input.TextArea
						placeholder='Note'
						rows={4}
						showCount
						maxLength={255}
					/>
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
									form.getFieldValue('assignee') === undefined ||
									form.getFieldValue('assetId') === undefined ||
									form.getFieldValue('assignDate') === undefined ||
									!!form.getFieldsError().filter(({ errors }) => errors.length)
										.length
								}
								style={{ marginRight: '5px' }}>
								Save
							</Button>
							<Button
								onClick={() => navigate('/manage-assignments')}
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

export default CreateNewAssignment;
