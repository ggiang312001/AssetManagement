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
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { createAsset } from '../../services/assetService';
import { getCategories } from '../../services/categoryService';

const CreateNewAsset = () => {
	const navigate = useNavigate();
	const [categories, setCategories] = useState(undefined);
	const [form] = Form.useForm();

	useEffect(() => {
		getCategories()
			.then((res) => {
				setCategories(res.data);
			})
			.catch((err) => {
				message.error(err.message);
			});
	}, []);

	const onFinish = (values) => {
		const data = {
			name: values.name,
			specification: values.specification,
			category: values.category,
			installedDate: values.installed_date.format('YYYY-MM-DD'),
			state: values.state,
		};
		createAsset(data)
			.then((res) => {
				message.success('Asset is created successfully');
				navigate('/manage-assets');
			})
			.catch((err) => {
				message.error(err.message);
			});
	};

	return (
		<div className='container-fluid' style={{ padding: '50px' }}>
			<Typography.Title id='create-asset-form-title' level={2} strong>
				Create new asset
			</Typography.Title>
			<Form
				form={form}
				name='create-new-asset'
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
						name: 'state',
						value: 'AVAILABLE',
					},
				]}>
				<Form.Item
					name='name'
					label='Name'
					rules={[
						{
							required: true,
							message: 'Asset name is required',
						},
						{
							min: 10,
							message: 'The length of name must be 10-255 characters',
						},
						{
							max: 255,
							message: 'The length of name must be 10-255 characters',
						},
						({ getFieldValue }) => ({
							validator(_, value) {
								if (value.trim() !== '') {
									return Promise.resolve();
								}
								return Promise.reject(
									new Error('The entered name must not contain only spaces'),
								);
							},
						}),
					]}>
					<Input placeholder='Asset Name' maxLength={255} />
				</Form.Item>

				<Form.Item
					name='category'
					label='Category'
					rules={[
						{
							required: true,
							message: 'Category is required',
						},
					]}>
					<Select placeholder='Category'>
						{categories &&
							categories.map((category) => (
								<>
									<Select.Option value={category.categoryId}>
										{category.name}
									</Select.Option>{' '}
								</>
							))}
					</Select>
				</Form.Item>

				<Form.Item
					name='specification'
					label='Specification'
					rules={[
						{
							required: true,
							message: 'Specification is required',
						},
						{
							min: 10,
							message:
								'The length of specification should be 10-255 characters',
						},
						{
							max: 255,
							message:
								'The length of specification should be 10-255 characters',
						},
						({ getFieldValue }) => ({
							validator(_, value) {
								if (value.trim() !== '') {
									return Promise.resolve();
								}
								return Promise.reject(
									new Error(
										'The entered specification must not contain only spaces',
									),
								);
							},
						}),
					]}>
					<Input.TextArea
						placeholder='Specifications'
						rows={4}
						showCount
						maxLength={255}
					/>
				</Form.Item>

				<Form.Item
					name='installed_date'
					label='Installed Date'
					rules={[
						{
							required: true,
							message: 'Installed date is required',
						},
					]}>
					<DatePicker
						inputReadOnly={true}
						disabledDate={(d) =>
							d.isSameOrBefore('2000-01-01') ||
							d.isAfter((new Date().getFullYear() + 1).toString())
						}
					/>
				</Form.Item>

				<Form.Item
					name='state'
					label='State'
					rules={[
						{
							required: true,
							message: 'State is required',
						},
					]}>
					<Radio.Group defaultValue='AVAILABLE'>
						<Radio value='AVAILABLE'>Available</Radio>
						<Radio value='NOT_AVAILABLE'>Not Available</Radio>
					</Radio.Group>
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
									form.getFieldValue('state') === undefined ||
									form.getFieldValue('name') === undefined ||
									form.getFieldValue('category') === undefined ||
									form.getFieldValue('specification') === undefined ||
									form.getFieldValue('installed_date') === undefined ||
									!!form.getFieldsError().filter(({ errors }) => errors.length)
										.length
								}
								style={{ marginRight: '5px' }}>
								Save
							</Button>
							<Button
								onClick={() => navigate('/manage-assets')}
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
export default CreateNewAsset;
