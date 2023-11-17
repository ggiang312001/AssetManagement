import React, { useEffect, useState, useCallback } from 'react';
import { Button, DatePicker, Form, message, Radio, Select } from 'antd';
import Input from 'antd/lib/input/Input';
import TextArea from 'antd/lib/input/TextArea';
import { useNavigate, useParams } from 'react-router-dom';
import { editAsset, getAssetDetail } from '../../services/assetService';
import moment from 'moment';
const EditAsset = () => {
	const [form] = Form.useForm();
	const { assetId } = useParams();

	const dateFormat = 'YYYY-MM-DD';
	const navigate = useNavigate();
	const [assetDetail, setAssetDetail] = useState({
		assetName: '',
		category: {
			categoryId: '',
			categoryName: '',
		},
		specification: '',
		installedDate: '',
		state: '',
	});
	const getDetail = useCallback(() => {
		getAssetDetail(assetId)
			.then((res) => {
				const { name, category, specification, installedDate, state } =
					res.data;
				setAssetDetail({
					assetName: name,
					category,
					specification,
					installedDate,
					state,
				});
				form.setFieldsValue({
					assetName: name,
					categoryName: category.name,
					specification,
					date: moment(installedDate),
					state,
				});
			})
			.catch((error) => {
				message.error(error.message);
			});
	}, [assetId, form]);

	useEffect(() => {
		if (assetId) {
			getDetail();
		}
	}, [getDetail, assetId]);

	const onFinish = (values) => {
		const data = {
			name: values.assetName,
			specification: values.specification,
			installedDate: new Date(Date.parse(values.date)).toISOString(),
			state: values.state,
		};
		editAsset(assetId, data)
			.then((res) => {
				message.success('Asset is edited successfully');
				navigate('/manage-assets');
			})
			.catch((err) => {
				message.error(err.message);
			});
		console.log('assetDetail', assetDetail);
	};

	return (
		<>
			<h1>Edit Asset</h1>
			<Form
				form={form}
				name='edit-asset'
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
				layout='horizontal'>
				<Form.Item
					label='Name'
					name='assetName'
					rules={[
						{
							required: true,
							message: 'Asset Name is required',
						},
						{
							min: 10,
							message: 'The length of asset name should be 10-255 characters',
						},
						{
							max: 255,
							message: 'The length of asset name should be 10-255 characters',
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
					label='Category'
					name='categoryName'
					rules={[
						{
							required: true,
							message: 'Category is required',
						},
					]}>
					<Select disabled={true}></Select>
				</Form.Item>
				<Form.Item
					label='Specification'
					name='specification'
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
									new Error('The entered name must not contain only spaces'),
								);
							},
						}),
					]}>
					<TextArea rows={4} placeholder='Specification' maxLength={255} />
				</Form.Item>

				<Form.Item
					label='Installed Date'
					name='date'
					rules={[
						{
							required: true,
							message: 'Installed Date is required',
						},
					]}>
					<DatePicker
						format={dateFormat}
						style={{ width: '100%' }}
						placeholder={''}
						inputReadOnly={true}
						disabledDate={(d) =>
							d.isSameOrBefore('2000-01-01') ||
							d.isAfter((new Date().getFullYear() + 1).toString())
						}
					/>
				</Form.Item>

				<Form.Item
					label='State'
					name='state'
					rules={[
						{
							required: true,
							message: 'State is required',
						},
					]}>
					<Radio.Group>
						<Radio value='AVAILABLE'>Available </Radio>
						<Radio value='NOT_AVAILABLE'> Not available </Radio>
						<Radio value='WAITING_FOR_RECYCLING'> Waiting for recycling </Radio>
						<Radio value='RECYCLED'> Recycled </Radio>
					</Radio.Group>
				</Form.Item>

				<Form.Item>
					<div style={{ marginLeft: '29%' }}>
						<Button
							htmlType='submit'
							type='danger'
							style={{ borderRadius: '5px', marginRight: '15px' }}>
							Save
						</Button>
						<Button
							onClick={() => navigate('/manage-assets')}
							style={{ borderRadius: '5px' }}>
							Cancel
						</Button>
					</div>
				</Form.Item>
			</Form>
		</>
	);
};

export default EditAsset;
