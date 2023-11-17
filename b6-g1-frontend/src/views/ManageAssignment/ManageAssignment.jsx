import React, { useState, useEffect, useCallback } from 'react';
import { Table, Modal, DatePicker } from 'antd';
import { CloseCircleOutlined } from '@ant-design/icons';
import { ReloadOutlined } from '@ant-design/icons';
import { Button, message, Space } from 'antd';
import { EditFilled } from '@ant-design/icons';
import CommonSelect from '../../components/Common/CommonSelect';
import CommonSearch from '../../components/Common/CommonSearch';
import {
	deleteAssignment,
	getAssignments,
	postReturnReq,
} from '../../services/assignmentService';
import { getAssignmentDetail } from '../../services/assignmentService';
import { useNavigate } from 'react-router-dom';
import './ManageAssignment.css';
import CommonHeaderSort from '../../components/Common/CommonHeaderSort';

const ManageAssignment = () => {
	const navigate = useNavigate();
	const [list, setList] = useState([]);
	const [loading, setLoading] = useState(false);
	const [dataSource, setDataSource] = useState([]);
	const [totalElements, setTotalElements] = useState();
	const [data, setData] = useState([]);

	const initialFilters = {
		searchTerm: '',
		dateFill: '',
		stateFill: '',
		pageSize: 10,
		pageNo: 1,
		sortBy: 'updatedAt',
		sortDir: 'desc',
	};
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
	const [filter, setFilter] = useState(initialFilters);
	const getData = useCallback(() => {
		setLoading(true);
		getAssignments(filter)
			.then((res) => {
				const listAsssignmentId = res.data.assignmentIdReq;
				setList(listAsssignmentId);
				const convertedData = res.data.content.map((data) => {
					const {
						assignmentId,
						assetId,
						assignee,
						assigner,
						assignDate,
						state,
					} = data;

					return {
						key: assignmentId,
						assignmentId,
						assetCode: `${assetId.assetId}`,
						assetName: `${assetId.name}`,
						assigner: `${assigner.username}`,
						assignee: `${assignee.username}`,
						assignDate,
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

	const onSearch = (value) => {
		const newFilter = {
			...filter,
			searchTerm: value.trim(),
			pageNo: 1,
		};
		setFilter(newFilter);
	};
	const onSelectState = (value) => {
		const newFilter = {
			...filter,
			stateFill: value,
			pageNo: 1,
		};
		const newFilter2 = {
			...filter,
			stateFill: '',
			pageNo: 1,
		};
		if (value === '') {
			setFilter(newFilter2);
		}
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
	const onSelectDate = (value) => {
		if (value === null) {
			const newFilter2 = {
				...filter,
				dateFill: '',
				pageNo: 1,
			};
			setFilter(newFilter2);
		} else {
			value = value.format('YYYY-MM-DD');

			const newFilter = {
				...filter,
				dateFill: value,
				pageNo: 1,
			};
			setFilter(newFilter);
		}
	};

	const [isModalOpen, setIsModalOpen] = useState(false);
	const showModal = () => {
		setIsModalOpen(true);
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};
	const onPageChange = (pageNo, pageSize) => {
		const newFilter = {
			...filter,
			pageNo,
			pageSize,
		};
		setFilter(newFilter);
	};
	const columns = [
		{
			title: (
				<CommonHeaderSort
					title='No.'
					dataIndex='assignmentId'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'assignmentId',
			key: dataSource.assignmentId,
			onCell: (record) => {
				return {
					onClick: () => {
						handleShowDetailModal(record);
					},
				};
			},
		},
		{
			title: 'Asset code',
			dataIndex: 'assetCode',
			key: dataSource.assetCode,
			sorter: (a, b) => a.assetCode.localeCompare(b.assetCode),
			onCell: (record) => {
				return {
					onClick: () => {
						handleShowDetailModal(record);
					},
				};
			},
		},
		{
			title: 'Asset Name',
			dataIndex: 'assetName',
			key: dataSource.assetName,
			sorter: (a, b) => a.assetName.localeCompare(b.assetName),
			onCell: (record) => {
				return {
					onClick: () => {
						handleShowDetailModal(record);
					},
				};
			},
		},
		{
			title: 'Assigned to',
			dataIndex: 'assignee',
			key: dataSource.assignee,
			sorter: (a, b) => a.assignee.localeCompare(b.assignee),
			onCell: (record) => {
				return {
					onClick: () => {
						handleShowDetailModal(record);
					},
				};
			},
		},
		{
			title: 'Assigned by',
			dataIndex: 'assigner',
			key: dataSource.assigner,
			sorter: (a, b) => a.assigner.localeCompare(b.assigner),
			onCell: (record) => {
				return {
					onClick: () => {
						handleShowDetailModal(record);
					},
				};
			},
		},
		{
			title: 'Assigned Date',
			dataIndex: 'assignDate',
			key: dataSource.assignDate,
			sorter: (a, b) => a.assignDate.localeCompare(b.assignDate),
			onCell: (record) => {
				return {
					onClick: () => {
						handleShowDetailModal(record);
					},
				};
			},
		},
		{
			title: 'State',
			dataIndex: 'state',
			key: dataSource.state,
			sorter: (a, b) => a.state.localeCompare(b.state),
			onCell: (record) => {
				return {
					onClick: () => {
						handleShowDetailModal(record);
					},
				};
			},
		},

		{
			title: '',
			key: '',
			render: (_, record) => (
				<Space size='small'>
					<Button
						disabled={record.state === 'Accepted' || record.state === 'Deleted' || record.state ==='Declined'}
						onClick={() => {
							navigate(
								`/manage-assignments/edit-assignment/${record.assignmentId}`,
							);
						}}>
						<EditFilled />
					</Button>
					<Button
						disabled={record.state === 'Accepted' || record.state === 'Deleted'}
						onClick={() => showModalDelete(record.assignmentId)}>
						<span style={{ color: 'red' }}>
							<CloseCircleOutlined />
						</span>
					</Button>
					<Button
						className='test'
						disabled={
							record.state !== 'Accepted' ||
							list.find((e) => e === record.assignmentId)
						}
						onClick={() => showModalReturn(record.assignmentId)}>
						<span style={{ color: 'blue' }}>
							<ReloadOutlined />
						</span>
					</Button>
				</Space>
			),
		},
	];

	const detailColumns = [
		{
			title: 'Label',
			dataIndex: 'label',
			key: 'label',
		},
		{
			title: 'Details',
			dataIndex: 'details',
			key: 'details',
		},
	];

	const [isModalDelete, setIsModalDelete] = useState(false);
	const [assignmentDelete, setAssignmentDelete] = useState(undefined);
	const [isModalReturn, setIsModalReturn] = useState(false);
	//return
	const [idReturn, setidReturn] = useState('');
	const showModalReturn = (id) => {
		setidReturn(id);
		setIsModalReturn(true);
	};

	const handleOkReturn = () => {
		postReturnReq(idReturn)
			.then(() => {
				message.success('Create a returning request succesfully');
				getData();
			})
			.catch((err) => message.error(err));
		setIsModalReturn(false);
	};

	const handleCancelReturn = () => {
		setIsModalReturn(false);
	};

	//delete
	const handleOkDelete = () => {
		deleteAssignment(assignmentDelete)
			.then((res) => {
				message.success('Assignment is deleted successfully');
				setIsModalDelete(false);
				// window.location.reload();
				// navigate('/manage-assignments');
				getData();
			})
			.catch((err) => {
				message.error(err.message);
			});
	};

	const handleCancelDelete = () => {
		setIsModalDelete(false);
	};

	const showModalDelete = (id) => {
		setAssignmentDelete(id);
		setIsModalDelete(true);
	};

	const handleShowDetailModal = (record) => {
		getAssignmentDetail(record.assignmentId)
			.then((res) => {
				const { assigner, assignee, assetId, assignDate, assignNote, state } =
					res.data;

				setData([
					{
						label: 'Asset ID',
						details: assetId.assetId,
					},
					{
						label: 'Asset Name',
						details: assetId.name,
					},
					{
						label: 'Assigner',
						details: assigner.username,
					},
					{
						label: 'Assignee',
						details: assignee.username,
					},
					{
						label: 'Specification',
						details: assetId.specification,
					},
					{
						label: 'Assign Date',
						details: assignDate,
					},
					{
						label: 'Assign Note',
						details: assignNote,
					},
					{
						label: 'State',
						details: titleCase(state.replaceAll('_', ' ')),
					},
				]);
			})
			.then(showModal);
	};

	return (
		<>
			<h1 className='asset-title'>Manage Assignments</h1>

			<Space size='middle' style={{ margin: '40px 0' }}>
				<CommonSelect
					options={[
						{ value: '', label: 'ALL' },
						{ value: 'ACCEPTED', label: 'Accepted' },
						{
							value: 'WAITING_FOR_ACCEPTANCE',
							label: 'Waiting For Acceptance',
						},
					]}
					placeholder='State'
					onSelect={onSelectState}
					allowClear
				/>

				<DatePicker
					allowClear={true}
					id='datepicker'
					showSearch
					style={{ width: 200 }}
					placeholder='Assigned Date'
					onChange={onSelectDate}
					inputReadOnly={true}></DatePicker>

				<CommonSearch placeholder='Search' onSearch={onSearch} />

				<Button
					style={{ float: 'right', backgroundColor: 'red', color: 'white' }}
					danger
					onClick={() => navigate(`/manage-assignments/create-assignment`)}>
					Create new assignment
				</Button>
			</Space>

			<Table
				key='1'
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
				showSorterTooltip={false}
			/>
			<Modal
				title='Detailed Assignment Information'
				open={isModalOpen}
				onCancel={handleCancel}
				footer={null}>
				<Table
					columns={detailColumns}
					dataSource={data}
					size='small'
					pagination={{
						hideOnSinglePage: true,
					}}
				/>
			</Modal>
			<Modal
				title='Are you sure?'
				open={isModalDelete}
				onOk={handleOkDelete}
				onCancel={handleCancelDelete}
				footer={null}>
				<p>Do you want to delete this assignment?</p>
				<Button
					className='button-modal'
					type='danger'
					key='back'
					onClick={handleOkDelete}>
					Delete
				</Button>

				<Button
					className='button-modal'
					key='submit'
					type=''
					loading={loading}
					onClick={handleCancelDelete}>
					Cancel
				</Button>
			</Modal>

			<Modal
				title='Are you sure?'
				open={isModalReturn}
				onOk={handleOkReturn}
				onCancel={handleCancelReturn}
				footer={null}>
				<p>Do you want to create a returning request for this asset?</p>
				<Button
					className='button-modal'
					type='danger'
					key='back'
					onClick={handleOkReturn}>
					Yes
				</Button>

				<Button
					className='button-modal'
					key='submit'
					type=''
					loading={loading}
					onClick={handleCancelReturn}>
					No
				</Button>
			</Modal>
		</>
	);
};
export default ManageAssignment;
