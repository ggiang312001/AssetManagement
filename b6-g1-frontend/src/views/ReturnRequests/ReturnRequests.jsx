import { CheckOutlined, CloseCircleOutlined } from '@ant-design/icons';
import { Button, DatePicker, message, Modal, Space, Table } from 'antd';
import React, { useState, useCallback, useEffect } from 'react';
import CommonHeaderSort from '../../components/Common/CommonHeaderSort';
import CommonSearch from '../../components/Common/CommonSearch';
import CommonSelect from '../../components/Common/CommonSelect';
import {
	completeReturnReq,
	deleteReturnReq,
	getReturnRequests,
} from '../../services/returnRequestService';

const ReturnRequests = () => {
	const [loading, setLoading] = useState(false);
	const [dataSource, setDataSource] = useState([]);
	const [totalElements, setTotalElements] = useState();
	function titleCase(str) {
		var splitStr = str.toLowerCase().split(' ');
		for (var i = 0; i < splitStr.length; i++) {
			// You do not need to check if i is larger than splitStr length, as your for does that for you
			// Assign it back to the array
			splitStr[i] =
				splitStr[i].charAt(0).toUpperCase() + splitStr[i].substring(1);
		}
		// Directly return the joined string
		return splitStr.join(' ');
	}
	const initialFilters = {
		searchTerm: '',
		returnedDate: '',
		state: '',
		pageSize: 10,
		pageNo: 1,
		sortBy: 'requestId',
		sortDir: 'asc',
	};
	const [filter, setFilter] = useState(initialFilters);

	const getData = useCallback(() => {
		setLoading(true);

		getReturnRequests(filter)
			.then((res) => {
				const convertedData = res.data.content.map((data) => {
					const {
						requestId,
						assignmentId: { assetId, assignDate },
						createdBy,
						acceptedBy,
						returnedDate,
						state,
					} = data;
					return {
						key: assetId,
						requestId,
						assetId: assetId.assetId,
						assetName: assetId.name,
						createdBy: createdBy.username,
						assignDate,
						acceptedBy: acceptedBy && acceptedBy.username,
						returnedDate: returnedDate && returnedDate.split('T')[0],
						state: titleCase(state.replaceAll('_', ' ')),
					};
				});
				setDataSource(convertedData);
				setTotalElements(res.data.totalElements);
			})
			.catch((error) => {
				message.error(error.message);
			});

		setLoading(false);
	}, [filter]);

	useEffect(() => {
		getData();
	}, [getData]);

	const onSelectState = (value) => {
		const newFilter = {
			...filter,
			state: value,
			pageNo: 1,
		};
		setFilter(newFilter);
	};

	const onSort = (dataIndex, sortDir) => {
		const newFilter = {
			...filter,
			sortBy: dataIndex,
			sortDir,
		};
		setFilter(newFilter);
	};

	const onSearch = (value) => {
		const newFilter = {
			...filter,
			searchTerm: value.trim(),
			pageNo: 1,
		};
		setFilter(newFilter);
	};

	const onSelectDate = (value) => {
		if (value === null) {
			const newFilter = {
				...filter,
				returnedDate: '',
				pageNo: 1,
			};
			setFilter(newFilter);
		} else {
			value = value.format('YYYY-MM-DD');

			const newFilter = {
				...filter,
				returnedDate: value,
				pageNo: 1,
			};
			setFilter(newFilter);
		}
	};

	const onPageChange = (pageNo, pageSize) => {
		const newFilter = {
			...filter,
			pageNo,
			pageSize,
		};
		setFilter(newFilter);
	};
	const [isModalAccept, setIsModalAccept] = useState(false);
	const [isModalDelete, setIsModalDelete] = useState(false);
	const [idAccept, setidAccept] = useState('');
	const [idDelete, setidDelete] = useState('');
	const showModalAccept = (id) => {
		setidAccept(id);
		setIsModalAccept(true);
	};

	const handleOkAccept = () => {
		completeReturnReq(idAccept)
			.then(() => {
				message.success(
					'Returning request is marked to completed successfully',
				);
				getData();
			})
			.catch((err) => message.error(err));
		setIsModalAccept(false);
	};

	const handleCancelAccept = () => {
		setIsModalAccept(false);
	};

	//Delete
	const showModalDelete = (id) => {
		setidDelete(id);
		setIsModalDelete(true);
	};

	const handleOkDelete = () => {
		deleteReturnReq(idDelete)
			.then(() => {
				message.success('Returning request is cancelled successfully');
				getData();
			})
			.catch((err) => message.error(err));
		setIsModalDelete(false);
	};

	const handleCancelDelete = () => {
		setIsModalDelete(false);
	};

	const columns = [
		{
			title: (
				<CommonHeaderSort
					title='No'
					dataIndex='requestId'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'requestId',
			key: 'requestId',
		},
		{
			title: (
				<CommonHeaderSort
					title='Asset Code'
					dataIndex='assetId'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'assetId',
			key: 'assetId',
		},
		{
			title: (
				<CommonHeaderSort
					title='Asset Name'
					dataIndex='assetName'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'assetName',
			key: 'assetName',
		},
		{
			title: (
				<CommonHeaderSort
					title='Requested By'
					dataIndex='createdBy'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'createdBy',
			key: 'createdBy',
		},
		{
			title: (
				<CommonHeaderSort
					title='Assigned Date'
					dataIndex='assignDate'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'assignDate',
			key: 'assignDate',
		},
		{
			title: (
				<CommonHeaderSort
					title='Accepted By'
					dataIndex='acceptedBy'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'acceptedBy',
			key: 'acceptedBy',
		},
		{
			title: (
				<CommonHeaderSort
					title='Returned Date'
					dataIndex='returnedDate'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'returnedDate',
			key: 'returnedDate',
		},
		{
			title: (
				<CommonHeaderSort
					title='State'
					dataIndex='state'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'state',
			key: 'state',
		},
		{
			dataIndex: 'action',
			key: 'action',
			render: (_, record) => (
				<Space size='small'>
					<Button
						disabled={record.state === 'Completed'}
						onClick={() => showModalAccept(`${record.requestId}`)}>
						<CheckOutlined />
					</Button>
					<Button
						disabled={record.state === 'Completed'}
						onClick={() => showModalDelete(`${record.requestId}`)}>
						<span style={{ color: 'red' }}>
							<CloseCircleOutlined />
						</span>
					</Button>
				</Space>
			),
		},
	];

	return (
		<>
			<h1>Request For Returning</h1>

			<Space size='middle' style={{ margin: '20px 0' }}>
				<CommonSelect
					options={[
						{ value: '', label: 'ALL' },
						{ value: 'WAITING_FOR_RETURNING', label: 'Waiting For Returning' },
						{ value: 'COMPLETED', label: 'Completed' },
					]}
					placeholder='State'
					onSelect={onSelectState}
				/>

				<DatePicker
					allowClear={true}
					id='datepicker'
					style={{ width: 200 }}
					placeholder='Returned Date'
					onChange={onSelectDate}
					inputReadOnly={true}></DatePicker>

				<CommonSearch
					placeholder='Search'
					onSearch={onSearch}
					value={filter.searchTerm}
				/>
			</Space>

			<Table
				columns={columns}
				dataSource={dataSource}
				loading={loading}
				pagination={{
					current: filter.pageNo,
					pageSize: filter.pageSize,
					total: totalElements,
					onChange: onPageChange,
					hideOnSinglePage: true,
				}}
			/>
			<Modal
				title='Are you sure?'
				open={isModalAccept}
				onOk={handleOkAccept}
				onCancel={handleCancelAccept}
				footer={null}>
				<p>Do you want to mark this returning request as "Completed"?</p>
				<Button
					className='button-modal'
					type='danger'
					key='back'
					onClick={handleOkAccept}>
					Yes
				</Button>

				<Button
					className='button-modal'
					key='submit'
					type=''
					loading={loading}
					onClick={handleCancelAccept}>
					No
				</Button>
			</Modal>

			<Modal
				title='Are you sure?'
				open={isModalDelete}
				onOk={handleOkAccept}
				onCancel={handleCancelDelete}
				footer={null}>
				<p>Do you want to cancel this returning request ?</p>
				<Button
					className='button-modal'
					type='danger'
					key='back'
					onClick={handleOkDelete}>
					Yes
				</Button>

				<Button
					className='button-modal'
					key='submit'
					type=''
					loading={loading}
					onClick={handleCancelDelete}>
					No
				</Button>
			</Modal>
		</>
	);
};

export default ReturnRequests;
