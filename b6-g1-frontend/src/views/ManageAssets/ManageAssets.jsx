import { Button, message, Space, Table, Modal } from 'antd';
import React, { useState, useEffect, useCallback } from 'react';
import { EditFilled, CloseCircleOutlined } from '@ant-design/icons';
import { useNavigate, Link } from 'react-router-dom';
import CommonHeaderSort from '../../components/Common/CommonHeaderSort';
import CommonSelect from '../../components/Common/CommonSelect';
import CommonSearch from '../../components/Common/CommonSearch';
import {
	getAssets,
	deleteAsset,
	checkDeleteAsset,
} from '../../services/assetService';
import { getCategories } from '../../services/categoryService';
import AssetDetailModal from '../../components/Modal/AssetDetailModal';

const ManageAssets = () => {
	const navigate = useNavigate();

	const [loading, setLoading] = useState(false);
	const [dataSource, setDataSource] = useState([]);
	const [totalElements, setTotalElements] = useState();
	const [categories, setCategories] = useState([]);
	const [isModalOpen, setIsModalOpen] = useState(false);
	const [assetId, setAssetId] = useState();
	const [isModalDelete, setIsModalDelete] = useState(false);
	const [canNotDeleteAsset, setCanNotDeleteAsset] = useState(false);
	const [idDelete, setIdDelete] = useState('');
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
		cateFill: 0,
		stateFill: '',
		pageSize: 10,
		pageNo: 1,
		sortBy: 'updatedAt',
		sortDir: 'desc',
	};
	const [filter, setFilter] = useState(initialFilters);

	const showModalDelete = (id) => {
		setIdDelete(id);
		setIsModalDelete(true);
	};

	const handleOkDelete = () => {
		deleteAsset(idDelete).then((res) => {
			message.success('Delete Asset Successfully!');
			const newFilter = {
				...filter,
				searchTerm: ' ',
				pageNo:
					totalElements % 10 === 1 &&
					totalElements / 10 + 1 === filter.pageNo + 0.1
						? filter.pageNo - 1
						: filter.pageNo,
			};
			setFilter(newFilter);
			setIsModalDelete(false);
		});
	};

	const handleCancelDelete = () => {
		setIsModalDelete(false);
	};

	const handleCancelCanNotDeleteAsset = () => {
		setCanNotDeleteAsset(false);
	};

	const getAssetsData = useCallback(() => {
		setLoading(true);
		getAssets(filter)
			.then((res) => {
				const convertedData = res.data.content.map((data) => {
					const { assetId, name, category, state } = data;

					return {
						key: assetId,
						assetId,
						name,
						category: category.name,
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

	const getCategoriesData = useCallback(() => {
		setLoading(true);
		getCategories()
			.then((res) => {
				const convertedData = res.data.map(({ categoryId, name }) => {
					return {
						value: categoryId,
						label: name,
					};
				});
				setCategories([{ value: 0, label: 'All' }, ...convertedData]);
			})
			.catch((error) => {
				message.error(error.message);
			});
		setLoading(false);
	}, []);

	useEffect(() => {
		getAssetsData();
	}, [getAssetsData]);

	useEffect(() => {
		getCategoriesData();
	}, [getCategoriesData]);

	const onSelectState = (value) => {
		const newFilter = {
			...filter,
			stateFill: value,
			pageNo: 1,
		};
		setFilter(newFilter);
	};

	const onSelectCategory = (value) => {
		const newFilter = {
			...filter,
			cateFill: value,
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
					title='Asset Code'
					dataIndex='assetId'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'assetId',
			key: 'assetId',
			onCell: (record) => {
				return {
					onClick: () => {
						setIsModalOpen(true);
						setAssetId(record.assetId);
					},
				};
			},
		},
		{
			title: (
				<CommonHeaderSort
					title='Asset Name'
					dataIndex='name'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'name',
			key: 'name',
			onCell: (record) => {
				return {
					onClick: () => {
						setIsModalOpen(true);
						setAssetId(record.assetId);
					},
				};
			},
		},
		{
			title: (
				<CommonHeaderSort
					title='Category'
					dataIndex='category'
					sortBy={filter.sortBy}
					onSort={onSort}
				/>
			),
			dataIndex: 'category',
			key: 'category',
			onCell: (record) => {
				return {
					onClick: () => {
						setIsModalOpen(true);
						setAssetId(record.assetId);
					},
				};
			},
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
			onCell: (record) => {
				return {
					onClick: () => {
						setIsModalOpen(true);
						setAssetId(record.assetId);
					},
				};
			},
		},
		{
			dataIndex: 'action',
			key: 'action',
			render: (_, record) => (
				<Space size='middle'>
					<Button
						disabled={record.state === 'Assigned'}
						onClick={() => {
							navigate(`/manage-assets/edit-asset/${record.assetId}`);
						}}>
						<EditFilled />
					</Button>
					<Button
						disabled={record.state === 'Assigned'}
						onClick={() => {
							setIdDelete(record.assetId);
							checkDeleteAsset(record.assetId).then((res) => {
								if (res.data === 0) {
									setCanNotDeleteAsset(true);
								} else {
									showModalDelete(`${record.assetId}`);
								}
							});
						}}>
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
			<h1 className='asset-title'>Manage Assets</h1>

			<Space size='middle' style={{ margin: '20px 0' }}>
				<CommonSelect
					options={[
						{ value: '', label: 'ALL' },
						{ value: 'ASSIGNED', label: 'Assigned' },
						{ value: 'AVAILABLE', label: 'Available' },
						{ value: 'NOT_AVAILABLE', label: 'Not Available' },
					]}
					placeholder='State'
					onSelect={onSelectState}
				/>
				<CommonSelect
					options={categories}
					placeholder='Categories'
					onSelect={onSelectCategory}
				/>

				<CommonSearch
					placeholder='Search'
					onSearch={onSearch}
					value={filter.searchTerm}
				/>

				<Button
					style={{ float: 'right', backgroundColor: 'red', color: 'white' }}
					danger
					onClick={() => navigate(`/manage-assets/create-asset/`)}>
					Create new asset
				</Button>
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

			<AssetDetailModal
				assetId={assetId}
				isModalOpen={isModalOpen}
				setIsModalOpen={setIsModalOpen}
			/>

			<Modal
				title='Can not delete asset'
				open={canNotDeleteAsset}
				onCancel={handleCancelCanNotDeleteAsset}
				footer={null}>
				Cannot delete the asset because it belongs to one or more historical
				assignments. If the asset is not able to be used anymore, please update
				its state in
				<Link to={`/manage-assets/edit-asset/${idDelete}`}>
					{' '}
					Edit Asset page
				</Link>
			</Modal>

			<Modal
				title='Are you sure?'
				open={isModalDelete}
				onOk={handleOkDelete}
				onCancel={handleCancelDelete}
				footer={null}>
				<p>Do you want to delete this asset?</p>
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
		</>
	);
};

export default ManageAssets;
